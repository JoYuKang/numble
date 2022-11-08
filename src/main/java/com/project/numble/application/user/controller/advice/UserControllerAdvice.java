package com.project.numble.application.user.controller.advice;

import com.project.numble.application.common.advice.ControllerAdviceUtils;
import com.project.numble.application.common.advice.ExceptionType;
import com.project.numble.application.user.controller.UserController;
import com.project.numble.application.user.service.util.UrlConnectionIOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = {UserController.class})
public class UserControllerAdvice {

    private final ControllerAdviceUtils utils;

    @ExceptionHandler(UrlConnectionIOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Map<String, String> urlConnectionIOExceptionHandler(UrlConnectionIOException e) {
        log.info("{}", e);
        return utils.getFailureResponse(ExceptionType.URL_CONNECTION_IO_EXCEPTION);
    }
}
