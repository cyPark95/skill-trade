package com.connect.skilltrade.security.domain.dto.command;

import com.connect.skilltrade.security.domain.oidc.domain.OAuthType;
import com.connect.skilltrade.security.domain.oidc.domain.dto.command.GetOIDCUserCommand;

public record GetAuthenticationCommand(
        OAuthType oauthtype,
        String code
) {

    public GetOIDCUserCommand toGetOIDCUserCommand() {
        return new GetOIDCUserCommand(oauthtype, code);
    }
}
