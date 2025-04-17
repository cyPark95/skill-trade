package com.connect.skilltrade.common.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionStatus {

    HttpStatus getHttpStatus();

    String getMessage();

    int getCode();
}
