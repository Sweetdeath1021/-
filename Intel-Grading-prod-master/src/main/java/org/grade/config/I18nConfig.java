package org.grade.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grade.constant.I18nProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * @author lixin
 * @date 2024/5/27
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(I18nProperties.class)
public class I18nConfig {
    private I18nProperties properties;

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames(properties.getMessagePaths());
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setCacheSeconds(properties.getCacheSeconds());
        return messageSource;
    }

    @Primary
    @Bean
    public LocaleResolver localeResolver() {
        Locale defaultLocale = new Locale(properties.getDefaultLanguage(), properties.getDefaultCountry());
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(defaultLocale);
        return localeResolver;
    }

    @Primary
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
        localeInterceptor.setParamName("lang");
        return localeInterceptor;
    }
}
