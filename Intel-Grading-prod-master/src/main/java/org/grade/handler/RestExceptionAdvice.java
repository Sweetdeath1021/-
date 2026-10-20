package org.grade.handler;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.grade.common.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@Slf4j
@ControllerAdvice
public class RestExceptionAdvice {
    @Autowired
    private MessageSource messageSource;

    public RestExceptionAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({ServiceException.class})
    protected ResponseEntity<Object> onServiceException(Exception exception) {
        log.error("[ServiceException]", exception);
        Locale locale = LocaleContextHolder.getLocale();
        String message = this.messageSource.getMessage(exception.getMessage(), null, exception.getMessage(), locale);
        if (message != null) {
            return new ResponseEntity<>(ImmutableMap.of("success", false, "error", message), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(ImmutableMap.of("success", false, "error", exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> onException(Exception exception) {
        log.error("[Exception]", exception);
        Locale locale = LocaleContextHolder.getLocale();
        String error = this.messageSource.getMessage("server.error", null, "server.error", locale);
        if (error != null) {
            return new ResponseEntity<>(ImmutableMap.of("success", false, "error", error), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(ImmutableMap.of("success", false, "error", exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
