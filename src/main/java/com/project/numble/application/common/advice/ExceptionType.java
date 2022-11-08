package com.project.numble.application.common.advice;

import lombok.Getter;

@Getter
public enum ExceptionType {
    EXCEPTION("exception.message"),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION("methodArgumentNotValidException.message"),
    USER_EMAIL_ALREADY_EXISTS_EXCEPTION("userEmailAlreadyExistsException.message"),
    USER_NOT_FOUND_EXCEPTION("userNotFoundException.message"),
    SIGN_IN_FAILURE_EXCEPTION("signInFailureException.message"),
    USER_NICKNAME_ALREADY_EXISTS_EXCEPTION("userNicknameAlreadyExistsException.message"),
    BOARD_NOT_EXISTS_EXCEPTION("boardNotExistsException.message"),
    URL_CONNECTION_IO_EXCEPTION("urlConnectionIOException.message"),
    USER_ALREADY_SIGN_OUT_EXCEPTION("userAlreadySignOutException.message")
    ;

    private final String message;

    ExceptionType(String message) {
        this.message = message;
    }
}
