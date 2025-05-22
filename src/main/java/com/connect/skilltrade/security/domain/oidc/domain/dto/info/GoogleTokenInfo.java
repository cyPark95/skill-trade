package com.connect.skilltrade.security.domain.oidc.domain.dto.info;

public record GoogleTokenInfo(
        String accessToken,
        int expiresIn,
        String idToken,
        String scope,
        String tokenType,
        String refreshToken
) {
}
