package com.connect.skilltrade.security.domain.user.domain.dto.command;

import com.connect.skilltrade.security.domain.oidc.domain.OAuthType;
import com.connect.skilltrade.security.domain.user.domain.AuthenticationUser;

public record GetAuthenticationUserCommand(
        String subject,
        String email,
        OAuthType OAuthType
) {

    public AuthenticationUser toDomain() {
        return new AuthenticationUser(subject, OAuthType, email);
    }
}
