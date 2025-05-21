package com.connect.skilltrade.domain.auth.dto;

public record OIDCUserInfo(
        String email,
        String name,
        String providerId
) {
}
