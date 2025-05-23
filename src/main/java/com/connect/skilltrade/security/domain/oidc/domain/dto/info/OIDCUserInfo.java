package com.connect.skilltrade.security.domain.oidc.domain.dto.info;

import com.connect.skilltrade.security.domain.oidc.domain.OAuthType;
import com.connect.skilltrade.security.domain.user.domain.dto.command.FindOAuthUserCommand;

public record OIDCUserInfo(
        String subject,
        String email
) {

    public FindOAuthUserCommand toFindOAuthUserCommand(OAuthType oAuthType) {
        return new FindOAuthUserCommand(oAuthType, subject, email);
    }
}
