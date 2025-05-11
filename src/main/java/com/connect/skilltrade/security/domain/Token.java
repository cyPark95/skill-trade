package com.connect.skilltrade.security.domain;

import lombok.Builder;

@Builder
public record Token(
        String accessToken,
        Long accessTokenExpiredAt,
        String refreshToken
) {
}
