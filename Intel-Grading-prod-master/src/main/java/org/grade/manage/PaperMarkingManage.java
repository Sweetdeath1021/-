package org.grade.manage;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.grade.bean.dto.IntelGradingDTO;
import org.grade.bean.request.Region.*;
import org.grade.bean.request.Task.TaskPageRequest;
import org.grade.bean.response.RegionResponse;
import org.grade.common.Result;
import org.grade.common.ServiceException;
import org.grade.model.AnswerSheet;
import org.grade.model.Region;
import org.grade.model.Task;
import org.grade.service.IAnswerSheetService;
import org.grade.service.IRegionService;
import org.grade.service.ITaskService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lixin
 * @date 2024/5/28
 */
@Component
public class PaperMarkingManage {
    @Resource
    private IAnswerSheetService answerSheetService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private IRegionService regionService;

    @Resource
    private ITaskService taskService;

    public Result queryTaskPage(TaskPageRequest request) {
        String userId = request.getUserId();
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Task::getUserId, userId)
                .orderByDesc(Task::getEndTime);

        Page<Task> page = new Page<>(request.getCurrent(), request.getSize());
        IPage<Task> iPage = taskService.page(page, queryWrapper);

        if (iPage.getTotal() == 0L) {
            return Result.fail("您尚未被分配阅卷任务");
        }

        return Result.ok(iPage);
    }

    public Result queryGradingRegion(RegionListRequest request) {
        // 根据userId，paperId和regionNumber，获取需要阅的图片集
        // 1.前置准备
        // 1.1.获取paperId、regionNumber和userId，定义map存储区域
        List<RegionResponse> list = new ArrayList<>();
        String paperId = request.getPaperId();
        Integer regionNumber = request.getRegionNumber();
        String userId = request.getUserId();
        // 1.2.stream流获取答题卡Id集合
        List<AnswerSheet> sheetList = answerSheetService.lambdaQuery()
                .eq(AnswerSheet::getPaperId, paperId).list();
        if (CollectionUtils.isEmpty(sheetList)) {
            throw new ServiceException("未查询到题目");
        }
        List<String> sheetIds = sheetList.stream()
                .map(AnswerSheet::getSheetId)
                .collect(Collectors.toList());
        // 1.3.根据sheetIds集合和regionNumber、userId获取需要评阅的对应区域
        for (String sheetId : sheetIds) {
            Region region = regionService.lambdaQuery()
                    .eq(Region::getSheetId, sheetId)
                    .eq(Region::getRegionNumber, regionNumber)
                    .isNotNull(Region::getUserId)
                    .eq(Region::getUserId, userId)
                    .one();
            if (ObjectUtil.isNull(region)) {
                continue;
            }
            RegionResponse build = RegionResponse.builder()
                    .regionImage(region.getRegionImage())
                    .isGraded(region.getIsGraded())
                    .regionId(region.getRegionId()).build();
            list.add(build);
        }
        if (list.isEmpty()) {
            return Result.fail("该题组尚未划分区域");
        }

        return Result.ok(list);
    }

    public Result queryRegionScore(RegionGetScoreRequest request) {
        String regionId = request.getRegionId();
        // 获取评分
        Integer regionScore = regionService.lambdaQuery()
                .eq(Region::getRegionId, regionId)
                .one().getRegionScore();
        return Result.ok(regionScore);
    }

    public Result updateRegionScore(RegionPutScoreRequest request) {
        String regionId = request.getRegionId();
        Integer regionScore = request.getRegionScore();
        boolean update = regionService.lambdaUpdate()
                .eq(Region::getRegionId, regionId)
                .set(Region::getRegionScore, regionScore)
                .set(Region::getIsGraded, true).update();
        if (!update) {
            throw new ServiceException("评分更新失败");
        }
        return Result.ok("评分更新成功");
    }

    public Result queryEvaluatedCount(RegionGetNumberRequest request) {
        String paperId = request.getPaperId();
        Integer regionNumber = request.getRegionNumber();
        String userId = request.getUserId();
        int num = 0;
        // 根据paperId查询所有答题卡
        List<AnswerSheet> list = answerSheetService.lambdaQuery()
                .eq(AnswerSheet::getPaperId, paperId).list();
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException("当前题组尚未划分阅卷区域");
        }
        List<String> sheetIds = list.stream()
                .map(AnswerSheet::getSheetId)
                .collect(Collectors.toList());

        for (String sheetId : sheetIds) {
            Region region = regionService.lambdaQuery()
                    .eq(Region::getSheetId, sheetId)
                    .eq(Region::getRegionNumber, regionNumber)
                    .isNotNull(Region::getUserId)
                    .eq(Region::getUserId, userId)
                    .one();
            if (ObjectUtil.isNotNull(region) && BooleanUtil.isTrue(region.getIsGraded())) {
                num++;
            }
        }

        return Result.ok(num);
    }

    public Result putIntelScore(RegionIntelScoreRequest request, MultipartFile images) {
        // 1.获取对应参数
        String regionImage = request.getRegionImage();
        String regionId = request.getRegionId();
        String filename = images.getOriginalFilename();

        // 2.与算法接口进行联调
        try {
            // 2.1.获取学生答案和标准答案的二进制
            byte[] studentBytes = HttpUtil.downloadBytes(regionImage);
            byte[] answerBytes = images.getBytes();

            // 2.2.获取学生答案的前缀
            if (StrUtil.isEmpty(filename)) {
                throw new RuntimeException("文件名为空");
            }
            // 将字节数组转换为Base64字符串
            String studentBase64 = Base64.getEncoder().encodeToString(studentBytes);
            String answerBase64 = Base64.getEncoder().encodeToString(answerBytes);

            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("student", studentBase64);
            paramMap.put("answer", answerBase64);

            IntelGradingDTO gradingDTO = new IntelGradingDTO(regionId, paramMap);
            rabbitTemplate.convertAndSend("intel.topic", "intel.grade", gradingDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return Result.ok("请求发送成功，等待评阅");
    }
}
