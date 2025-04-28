package com.connect.skilltrade.security.domain;

import com.connect.skilltrade.common.exception.ExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SecurityExceptionStatus implements ExceptionStatus {

    ACCESS_TOKEN_CLAIMS_NULL(HttpStatus.BAD_REQUEST, "엑세스 토큰에 포함될 정보는 null일 수 없습니다.", 101)
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;
}
