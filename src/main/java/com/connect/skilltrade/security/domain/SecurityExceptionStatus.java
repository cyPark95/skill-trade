package com.connect.skilltrade.security.domain;

import com.connect.skilltrade.common.exception.ExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SecurityExceptionStatus implements ExceptionStatus {

    ACCESS_TOKEN_SUBJECT_NULL(HttpStatus.BAD_REQUEST, "엑세스 토큰에 포함될 Subject 정보는 null일 수 없습니다.", 101),
    ACCESS_TOKEN_ROLE_CLAIM_NULL(HttpStatus.BAD_REQUEST, "엑세스 토큰에 포함될 역할 정보는 null 또는 빈 값일 수 없습니다.", 107),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;
}
