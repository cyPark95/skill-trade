package com.connect.skilltrade.security.domain.oidc.domain;

import com.connect.skilltrade.common.exception.BusinessException;
import com.connect.skilltrade.security.domain.SecurityExceptionStatus;
import com.connect.skilltrade.security.domain.oidc.domain.dto.command.GetOIDCUserCommand;
import com.connect.skilltrade.security.domain.oidc.domain.dto.info.OIDCUserInfo;
import com.connect.skilltrade.security.domain.oidc.domain.provider.OIDCProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OIDCService {

    private final List<OIDCProvider> oidcProviders;

    public OIDCUserInfo getOIDCUser(GetOIDCUserCommand command) {
        OIDCProvider provider = getProvider(command.OAuthType());
        return provider.getOIDCUserInfo(command.code());
    }

    private OIDCProvider getProvider(OAuthType OAuthType) {
        return oidcProviders.stream()
                .filter(provider -> provider.supports(OAuthType))
                .findFirst()
                .orElseThrow(() -> new BusinessException(SecurityExceptionStatus.NOT_SUPPORT_OIDC_SOCIAL_TYPE, OAuthType.name()));
    }
}
