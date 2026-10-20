package org.grade.listeners;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.grade.bean.dto.IntelGradingDTO;
import org.grade.bean.dto.ScoreDTO;
import org.grade.common.ServiceException;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.HashMap;

@Component
@Slf4j
public class InvokeCloudListener {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Value("${api.grade.url}")
    private String GRADE_URL;
    @Value("${api.grade.request.header}")
    private String GRADE_REQUEST_HEADER;
    @Value("${api.grade.request.secret}")
    private String GRADE_REQUEST_SECRET;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "grade.queue", durable = "true"),
            exchange = @Exchange(name = "intel.topic", type = ExchangeTypes.TOPIC),
            key = "intel.grade",
            arguments = @Argument(name = "x-queue-mode", value = "lazy")
    ))
    public void intelGrading(IntelGradingDTO gradingDTO) {
        // 3.利用hutool工具的HttpRequest请求调用python接口
        String url = GRADE_URL;

        // 解码Base64字符串
        String studentBase64 = (String) gradingDTO.getParamMap().get("student");
        String answerBase64 = (String) gradingDTO.getParamMap().get("answer");

        byte[] studentBytes = Base64.getDecoder().decode(studentBase64);
        byte[] answerBytes = Base64.getDecoder().decode(answerBase64);

        try {
            Path tempStudentPath = Files.createTempFile("studentTemp", null);
            Path tempAnswerPath = Files.createTempFile("answerTemp", null);
            Files.write(tempStudentPath, studentBytes, StandardOpenOption.CREATE);
            Files.write(tempAnswerPath, answerBytes, StandardOpenOption.CREATE);

            File student = tempStudentPath.toFile();
            File answer = tempAnswerPath.toFile();

            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("student", student);
            paramMap.put("answer", answer);

            HttpResponse execute = HttpRequest.post(url)
                    .header(GRADE_REQUEST_HEADER, GRADE_REQUEST_SECRET)
                    .form(paramMap)
                    .execute();

            // 3.1.判断接口是否调用成功
            int status = execute.getStatus();

            if (status != 200) {
                throw new ServiceException("执行失败，错误码：" + status);
            }
            // 3.2.将返回值转为Bean，发送给回调函数
            String json = execute.body();
            if (StrUtil.isEmpty(json)) {
                throw new ServiceException("响应体为空，请检查接口状态");
            }

            log.info("阅卷接口响应体{}", json);
            ScoreDTO scoreDTO = JSONUtil.toBean(json, ScoreDTO.class);
            scoreDTO.setRegionId(gradingDTO.getRegionId());
            rabbitTemplate.convertAndSend("intel.fanout", "", scoreDTO);

            // 3.3.删除临时文件
            Files.delete(tempStudentPath);
            Files.delete(tempAnswerPath);
        } catch (Exception e) {
            throw new ServiceException("云端接口请求异常", e);
        }
    }
}
