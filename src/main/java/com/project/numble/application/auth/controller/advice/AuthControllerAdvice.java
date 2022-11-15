package com.project.numble.application.auth.controller.advice;

import com.project.numble.application.auth.controller.AuthController;
import com.project.numble.application.auth.exception.UserAlreadySignOutException;
import com.project.numble.application.auth.exception.WithdrawalUserException;
import com.project.numble.application.auth.service.exception.SignInFailureException;
import com.project.numble.application.common.advice.ControllerAdviceUtils;
import com.project.numble.application.common.advice.ExceptionType;
import com.project.numble.application.user.service.exception.UserEmailAlreadyExistsException;
import com.project.numble.application.user.service.exception.UserNicknameAlreadyExistsException;
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

    @ExceptionHandler(SignInFailureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> signInFailureExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.SIGN_IN_FAILURE_EXCEPTION);
    }

    @ExceptionHandler(UserNicknameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> userNicknameAlreadyExistsExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.USER_NICKNAME_ALREADY_EXISTS_EXCEPTION);
    }

    @ExceptionHandler(UserAlreadySignOutException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> userAlreadySignOutExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.USER_ALREADY_SIGN_OUT_EXCEPTION);
    }

    @ExceptionHandler(WithdrawalUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> withdrawalUserExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.WITHDRAWAL_USER_EXCEPTION);
    }
}
