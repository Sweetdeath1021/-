package org.grade.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss.file")
public class OSSProperties {

    /**
     * 对应地域地址
     */
    private String endpoint;

    /**
     * 账号
     */
    private String keyId;

    /**
     * 密码
     */
    private String keySecret;

    /**
     * 创建的bucket名
     */
    private String bucketName;
}