package com.connect.skilltrade.example.exception.domain;

import com.connect.skilltrade.common.exception.ExceptionStatus;
import org.springframework.http.HttpStatus;

public enum SampleExceptionStatus implements ExceptionStatus {

    NORMAL(HttpStatus.BAD_REQUEST, "NORMAL", -100),
    SAMPLE_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "Exception: [%s] %s", -99),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;

    SampleExceptionStatus(HttpStatus httpStatus, String message, int code) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getCode() {
        return code;
    }
}
