package com.connect.skilltrade.security.domain;

import com.connect.skilltrade.common.exception.ExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SecurityExceptionStatus implements ExceptionStatus {

    INTERNAL_AUTHORIZATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "인가 처리 중 예외가 발생했습니다", 100),
    ACCESS_TOKEN_SUBJECT_NULL_OR_EMPTY(HttpStatus.BAD_REQUEST, "엑세스 토큰에 포함될 Subject 정보가 null 또는 빈 값 입니다.", 101),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰[%s] 입니다.", 102),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰[%s] 입니다.", 103),
    INVALID_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "유효하지 않은 인가 정보 입니다.", 104),
    ACCESS_TOKEN_ROLE_CLAIM_NULL_OR_EMPTY(HttpStatus.BAD_REQUEST, "엑세스 토큰에 포함될 역할 정보가 null 또는 빈 값 입니다.", 107),
    ACCESS_TOKEN_SUBJECT_NOT_FOUND(HttpStatus.BAD_REQUEST, "엑세스 토큰에 포함된 Subject 정보를 찾을 수 없습니다.", 108),
    ACCESS_TOKEN_ROLE_CLAIM_NOT_FOUND(HttpStatus.BAD_REQUEST, "엑세스 토큰에 포함된 역할 정보를 찾을 수 없습니다.", 109),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;
}
