package com.connect.skilltrade.security.domain.oidc.domain.provider;

import com.connect.skilltrade.security.domain.oidc.domain.dto.info.OIDCUserInfo;
import com.connect.skilltrade.security.domain.oidc.domain.OAuthType;

public interface OIDCProvider {

    boolean supports(OAuthType type);

    String getAuthorizationUrl();

    OIDCUserInfo getOIDCUserInfo(String code);
}
