package org.grade.utils;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import org.grade.common.ServiceException;
import org.grade.constant.OSSProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;

import static org.grade.constant.OSSConstant.*;

@Component
public class OSSUtil {
    @Resource
    private OSSProperties OSSProperties;

    /**
     * 上传图片
     * @param originalFileName
     * @param inputStream
     * @param fileUrl
     * @return
     */
    public String upload(String originalFileName, InputStream inputStream, String fileUrl) {
        if (StrUtil.isEmpty(originalFileName) || StrUtil.isEmpty(fileUrl)) {
            throw new ServiceException("文件名或存储路径为空！");
        }

        String endpoint = OSSProperties.getEndpoint();
        String keyID = OSSProperties.getKeyId();
        String keySecret = OSSProperties.getKeySecret();
        String bucketName = OSSProperties.getBucketName();

        // UUID生成文件名并拼接拓展名
        // 避免重名，使用uuid+原文件后缀名
        UUID uuid = UUID.randomUUID();
        String fileName = originalFileName.indexOf('.') == -1
                ? uuid + originalFileName
                : uuid + originalFileName.substring(originalFileName.lastIndexOf("."));

        // 拼接文件全路径
        String filePath = fileUrl + fileName;

        // 上传文件到 OSS
        OSS ossClient = new OSSClient(endpoint, keyID, keySecret);
        ossClient.putObject(bucketName, filePath, inputStream);

        // 文件访问路径
        String storagePath = URL_HTTPS + bucketName
                + URL_POINT + endpoint + FILE_SEPARATOR + filePath;
        // 关闭ossClient
        ossClient.shutdown();

        // 把上传到oss的路径返回
        return storagePath;
    }
}
