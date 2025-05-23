package com.connect.skilltrade.security.presentation.dto.response;

public record TokenResponse(
        String accessToken,
        Long accessTokenExpiredAt,
        String refreshToken
) {
}
