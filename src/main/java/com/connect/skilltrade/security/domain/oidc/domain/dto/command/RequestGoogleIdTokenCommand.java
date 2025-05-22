package com.connect.skilltrade.security.domain.oidc.domain.dto.command;

public record RequestGoogleIdTokenCommand(
        String code,
        String clientId,
        String clientSecret,
        String redirectUri,
        String grantType
) {
}
