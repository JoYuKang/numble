package com.project.numble.application.common.advice;

import lombok.Getter;

@Getter
public enum ExceptionType {
    EXCEPTION("exception.message"),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION("methodArgumentNotValidException.message"),
    USER_EMAIL_ALREADY_EXISTS_EXCEPTION("userEmailAlreadyExistsException.message")
    ;

    private final String message;

    ExceptionType(String message) {
        this.message = message;
    }
}
