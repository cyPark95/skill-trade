package com.connect.skilltrade.security.domain.oidc.domain.dto.info;

public record OIDCUserInfo(
        String subject,
        String email,
        String name
) {
}
