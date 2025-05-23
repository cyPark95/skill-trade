package com.connect.skilltrade.security.domain.oidc.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthType {

    GOOGLE("구글"),
    APPLE("애플"),
    KAKAO("카카오"),
    ;

    private final String description;
}
