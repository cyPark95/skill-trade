package com.connect.skilltrade.security.domain.oidc.domain.dto.command;

import com.connect.skilltrade.security.domain.oidc.domain.OAuthType;

public record GetOIDCUserCommand(
        OAuthType OAuthType,
        String code
) {
}
