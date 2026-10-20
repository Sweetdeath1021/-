package org.grade.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lixin
 * @date 2024/5/27
 */
@Data
@ConfigurationProperties(prefix = "i18n")
public class I18nProperties {
    /**
     * 缓存秒数
     */
    private int cacheSeconds = 3600;

    /**
     * 默认语言
     */
    private String defaultLanguage = "zh";

    /**
     * 默认国家
     */
    private String defaultCountry = "CN";

    /**
     * 资源文件配置路径
     */
    private String[] messagePaths = {"classpath:messages/messages"};
}