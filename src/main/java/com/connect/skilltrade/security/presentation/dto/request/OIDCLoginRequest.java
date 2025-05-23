package com.connect.skilltrade.security.presentation.dto.request;

import com.connect.skilltrade.security.domain.dto.command.GetAuthenticationCommand;
import com.connect.skilltrade.security.domain.oidc.domain.OAuthType;

public record OIDCLoginRequest(
        OAuthType oAuthType,
        String code
) {

    public GetAuthenticationCommand toCommand() {
        return new GetAuthenticationCommand(oAuthType, code);
    }
}
