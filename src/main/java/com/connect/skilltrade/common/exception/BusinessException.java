package com.connect.skilltrade.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ExceptionStatus exceptionStatus;

    public BusinessException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public BusinessException(ExceptionStatus exceptionStatus, String... args) {
        super(String.format(exceptionStatus.getMessage(), args));
        this.exceptionStatus = exceptionStatus;
    }

    public BusinessException(Exception e, ExceptionStatus exceptionStatus, String... args) {
        super(String.format(exceptionStatus.getMessage(), args), e);
        this.exceptionStatus = exceptionStatus;
    }
}
