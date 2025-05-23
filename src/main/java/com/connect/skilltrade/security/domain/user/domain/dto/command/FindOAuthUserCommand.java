package com.connect.skilltrade.security.domain.user.domain.dto.command;

import com.connect.skilltrade.security.domain.oidc.domain.OAuthType;
import com.connect.skilltrade.security.domain.user.domain.User;

public record FindOAuthUserCommand(
        OAuthType OAuthType,
        String subject,
        String email
) {

    public User toDomain() {
        return User.createInitUser(subject, OAuthType, email);
    }
}
