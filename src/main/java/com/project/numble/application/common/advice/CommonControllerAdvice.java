package com.project.numble.application.common.advice;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CommonControllerAdvice {

    private final ControllerAdviceUtils utils;

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> bindException(BindException e) {
        log.info("{}", e.getBindingResult().getFieldError());
        return utils.getFailureResponse(ExceptionType.METHOD_ARGUMENT_NOT_VALID_EXCEPTION, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Map<String, String> exceptionHandler(Exception e) {
        log.info("{}", e);
        return utils.getFailureResponse(ExceptionType.EXCEPTION);
    }
}
