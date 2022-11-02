package com.project.numble.application.auth.controller.advice;

import com.project.numble.application.auth.controller.AuthController;
import com.project.numble.application.common.advice.ControllerAdviceUtils;
import com.project.numble.application.common.advice.ExceptionType;
import com.project.numble.application.user.service.exception.UserEmailAlreadyExistsException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = {AuthController.class})
public class AuthControllerAdvice {

    private final ControllerAdviceUtils utils;

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> userEmailAlreadyExistsExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.USER_EMAIL_ALREADY_EXISTS_EXCEPTION);
    }
}
