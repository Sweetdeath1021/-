package org.grade.common;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.grade.bean.dto.FileDTO;
import org.grade.model.AnswerSheet;
import org.grade.model.TestPaper;
import org.grade.service.IAnswerSheetService;
import org.grade.service.ITestPaperService;
import org.grade.utils.CompressUtil;
import org.grade.utils.OSSUtil;
import org.grade.utils.PyUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import static org.grade.constant.OSSConstant.FILE_SEPARATOR;
import static org.grade.constant.PythonConstant.QR_SCRIPT_PATH;

/**
 * @author lixin
 * @date 2024/5/28
 */
@Component
public class UploadSheetTask implements Runnable {
    @Resource
    private OSSUtil ossUtil;
    @Resource
    private CompressUtil compressUtil;
    @Resource
    private ITestPaperService testPaperService;
    @Resource
    private IAnswerSheetService answerSheetService;

    private FileDTO fileDTO;

    public UploadSheetTask() {
        // 无参构造函数，用于Spring自动注入依赖
    }

    public UploadSheetTask setFileDTO(FileDTO fileDTO) {
        this.fileDTO = fileDTO;
        return this;
    }

    @Override
    public void run() {
        // 上传每个班级的答题卡
        for (Map.Entry<String, InputStream> file: fileDTO.getMap().entrySet()) {
            // 获取班级名称
            String className = file.getKey().substring(0, file.getKey().indexOf('_'));
            // 拼接存储路径
            String storageUrl = fileDTO.getStorageUrl() +
                    className + FILE_SEPARATOR;
            uploadSheets(file.getKey(), file.getValue(), storageUrl, fileDTO.getPaperId());
        }

        // 答题卡全部上传成功后，更新状态
        testPaperService.lambdaUpdate()
                .set(TestPaper::getSheetStatus, 1)
                .eq(TestPaper::getPaperId, fileDTO.getPaperId())
                .update();
    }

    public void uploadSheets(String originalFileName, InputStream inputStream, String storageUrl, String paperId) {
        try {
            // 获取源文件名
            if (StrUtil.isEmpty(originalFileName) || StrUtil.isEmpty(storageUrl)) {
                throw new ServiceException("文件名或存储路径为空！");
            }

            // 将文件存储进temp
            Path tempFile = Files.createTempFile("temp", null);
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

            // 将原始文件名全部转为小写，便于比较格式
            String lowerCaseFileName = originalFileName.toLowerCase();
            // 调用工具类将每个班级的压缩包解压，传入oss
            Map<String, InputStream> streamMap = compressUtil.decompress(tempFile, lowerCaseFileName);
            // 加锁保证数据一致
            Integer sheetNum;
            synchronized (this) {
                // 获取当前已上传班级的答题卡数量
                sheetNum = testPaperService.lambdaQuery()
                        .eq(TestPaper::getPaperId, paperId)
                        .one().getSheetNum();
                if (ObjectUtil.isEmpty(sheetNum)) {
                    sheetNum = 0;
                }
                // 将答题卡数量更新
                testPaperService.lambdaUpdate()
                        .set(TestPaper::getSheetNum, sheetNum + streamMap.size())
                        .eq(TestPaper::getPaperId, paperId)
                        .update();
            }

            for (Map.Entry<String, InputStream> entry : streamMap.entrySet()) {
                // 上传图片，并获取每张图片的存储路径
                String url = ossUtil.upload(entry.getKey(), entry.getValue(), storageUrl);
                // 调用py脚本，根据图片的条形码扫出学号
                String number = PyUtil.QrScript(QR_SCRIPT_PATH, url);
                answerSheetService.save(new AnswerSheet(paperId, number, url, DateTime.now(), 0));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
