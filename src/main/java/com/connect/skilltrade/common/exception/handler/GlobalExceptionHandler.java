package com.connect.skilltrade.common.exception.handler;

import com.connect.skilltrade.common.response.CommonExceptionStatus;
import com.connect.skilltrade.common.response.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String exceptionMessage = getBindingResultFieldErrorsMessage(e);
        log.warn("[HandleMethodArgumentNotValidException] message = {}", exceptionMessage, e);

        ExceptionResponse response = new ExceptionResponse(
                exceptionMessage,
                CommonExceptionStatus.COMMON_INVALID_PARAMETER.getCode()
        );

        return new ResponseEntity<>(response, CommonExceptionStatus.COMMON_INVALID_PARAMETER.getHttpStatus());
    }

    private String getBindingResultFieldErrorsMessage(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        return bindingResult.getFieldErrors().stream()
                .map(fieldError -> String.format("[%s] %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
