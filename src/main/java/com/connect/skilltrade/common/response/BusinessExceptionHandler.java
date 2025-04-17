package com.connect.skilltrade.common.response;

import com.connect.skilltrade.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException e) {
        log.warn("[BusinessException] message = {}", e.getMessage(), e.getCause());
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), e.getExceptionStatus().getCode());
        return new ResponseEntity<>(response, e.getExceptionStatus().getHttpStatus());
    }
}
