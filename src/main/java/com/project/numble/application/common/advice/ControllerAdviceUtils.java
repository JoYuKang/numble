package com.project.numble.application.common.advice;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@RequiredArgsConstructor
public class ControllerAdviceUtils {

    private final MessageSource messageSource;
    private final Map<String, String> errorMessageMap = new HashMap<>();

    public Map<String, String> getFailureResponse(ExceptionType exceptionType) {
        errorMessageMap.put("message", getMessage(exceptionType.getMessage()));

        return errorMessageMap;
    }

    public Map<String, String> getFailureResponse(ExceptionType exceptionType, Object... args) {
        errorMessageMap.put("message", getMessage(exceptionType.getMessage(), args));

        return errorMessageMap;
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

}
