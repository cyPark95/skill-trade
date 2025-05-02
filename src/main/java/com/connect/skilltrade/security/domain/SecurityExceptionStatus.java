package com.connect.skilltrade.security.domain;

import com.connect.skilltrade.common.exception.ExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SecurityExceptionStatus implements ExceptionStatus {

    INTERNAL_AUTH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "인증 처리 중 오류가 발생했습니다", 100),
    ACCESS_TOKEN_CLAIMS_NULL(HttpStatus.BAD_REQUEST, "엑세스 토큰 클레임 정보가 null 입니다.", 101),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰[%s] 입니다.", 102),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰[%s] 입니다.", 103),
    INVALID_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보[%s] 입니다.", 104),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;
}
