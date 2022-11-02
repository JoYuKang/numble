package com.project.numble.core.security.exception;

public class UnsupportedSecurityOperationException extends RuntimeException {

    private static final String MESSAGE = "지원하지 않는 기능입니다.";

    public UnsupportedSecurityOperationException() {
        super(MESSAGE);
    }
}
