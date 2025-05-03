package com.connect.skilltrade.security.domain;

public record Token(
        String accessToken,
        Long accessTokenExpiredAt,
        String refreshToken
) {
}
