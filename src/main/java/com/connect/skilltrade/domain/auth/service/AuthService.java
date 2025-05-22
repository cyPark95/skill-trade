package com.connect.skilltrade.domain.auth.service;

import com.connect.skilltrade.security.domain.oidc.domain.dto.info.OIDCUserInfo;
import com.connect.skilltrade.security.domain.oidc.domain.OAuthType;
import com.connect.skilltrade.security.domain.oidc.domain.provider.GoogleOIDCProvider;
import com.connect.skilltrade.security.domain.oidc.domain.provider.OIDCProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    private final List<OIDCProvider> oidcProviders;

    public AuthService(List<OIDCProvider> oidcProviders) {
        this.oidcProviders = oidcProviders;
    }


    public OIDCProvider getService(String socialType) {
        return oidcProviders.stream()
                .filter(url -> url.supports(OAuthType.valueOf(socialType)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원되지 않는 소셜로그인 형식 : " + socialType));
    }

    public OIDCUserInfo getUserInfo(String code, String socialType) {
        OIDCProvider provider = getService(socialType);

        if (provider instanceof GoogleOIDCProvider googleOIDCProvider) {
            return googleOIDCProvider.getOIDCUserInfo(code);
        }
        throw new IllegalArgumentException("xxx");
    }
}
