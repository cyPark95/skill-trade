package com.connect.skilltrade.security.domain.dto.info;

public record TokenInfo(
        String accessToken,
        Long accessTokenExpiredAt,
        String refreshToken
) {
}
