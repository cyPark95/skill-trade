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
    ACCESS_TOKEN_SUBJECT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "엑세스 토큰에 포함된 Subject 정보를 찾을 수 없습니다.", 108),
    ACCESS_TOKEN_ROLE_CLAIM_NOT_FOUND(HttpStatus.UNAUTHORIZED, "엑세스 토큰에 포함된 역할 정보를 찾을 수 없습니다.", 109),
    NOT_SUPPORT_OIDC_SOCIAL_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 OIDC 소셜 타입[%s] 입니다.", 110),
    FAIL_EXCHANGE_GOOGLE_ID_TOKEN(HttpStatus.UNAUTHORIZED, "승인 코드[%s]로 Google ID 토큰과 엑세스 토큰 교환에 실패했습니다.", 111),
    INVALID_GOOGLE_ID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Google ID 토큰[%s] 입니다.", 112),
    GOOGLE_ID_TOKEN_PAYLOAD_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Google ID 토큰에 포함된 Payload를 찾을 수 없습니다.", 113),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;
}
