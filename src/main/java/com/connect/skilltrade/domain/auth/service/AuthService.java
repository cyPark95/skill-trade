package com.connect.skilltrade.domain.auth.service;

import com.connect.skilltrade.domain.auth.dto.OIDCUserInfo;
import com.connect.skilltrade.domain.auth.provider.GoogleOIDCProvider;
import com.connect.skilltrade.domain.auth.provider.OIDCProvider;
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
                .filter(url -> url.supports(socialType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원되지 않는 소셜로그인 형식 : " + socialType));
    }

    public OIDCUserInfo getUserInfo(String code, String socialType) {
        OIDCProvider provider = getService(socialType);

        if (provider instanceof GoogleOIDCProvider googleOIDCProvider) {
            return googleOIDCProvider.getUserInfo(code);
        }
        throw new IllegalArgumentException("xxx");
    }
}
