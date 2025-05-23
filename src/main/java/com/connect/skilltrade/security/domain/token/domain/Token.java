package com.connect.skilltrade.security.domain.token.domain;

public record Token(
        String accessToken,
        Long accessTokenExpiredAt,
        String refreshToken
) {
}
