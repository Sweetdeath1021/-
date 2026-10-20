package org.grade.common;

import org.grade.bean.dto.FileDTO;
import org.grade.model.PaperImage;
import org.grade.model.TestPaper;
import org.grade.service.IPaperImageService;
import org.grade.service.ITestPaperService;
import org.grade.utils.OSSUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.Map;

/**
 * @author lixin
 * @date 2024/5/28
 */
@Component
public class UploadPaperTask implements Runnable {
    @Resource
    private OSSUtil ossUtil;
    @Resource
    private IPaperImageService paperImageService;
    @Resource
    private ITestPaperService testPaperService;
    private FileDTO fileDTO;

    public UploadPaperTask() {
        // 无参构造函数，用于Spring自动注入依赖
    }

    public UploadPaperTask setFileDTO(FileDTO fileDTO) {
        this.fileDTO = fileDTO;
        return this;
    }

    @Override
    public void run() {
        // 批量上传样卷
        for (Map.Entry<String, InputStream> file: fileDTO.getMap().entrySet()) {
            // 上传文件并获取oss地址
            String ossUrl = ossUtil.upload(file.getKey(), file.getValue(), fileDTO.getStorageUrl());
            paperImageService.save(new PaperImage(ossUrl, fileDTO.getPaperId()));
        }
        // 样卷全部上传成功后，更新状态
        testPaperService.lambdaUpdate()
                .set(TestPaper::getPaperStatus, 1)
                .eq(TestPaper::getPaperId, fileDTO.getPaperId())
                .update();
    }
}
