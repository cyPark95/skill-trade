package com.connect.skilltrade.domain.user.provider;

import com.connect.skilltrade.common.exception.BusinessException;
import com.connect.skilltrade.domain.user.Provider;
import com.connect.skilltrade.security.domain.SecurityExceptionStatus;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Component
public class OidcProviderFactory {

    private final Map<Provider, OidcProvider> authProviderMap;
    private final GoogleAuthProvider googleAuthProvider;

    public OidcProviderFactory( GoogleAuthProvider googleAuthProvider) {
        authProviderMap = new EnumMap<>(Provider.class);
        this.googleAuthProvider = googleAuthProvider;

        initialize();
    }

    private void initialize() {
        authProviderMap.put(Provider.GOOGLE, googleAuthProvider);
    }

    public String getProviderId(Provider provider, String idToken) {
        return getProvider(provider).getProviderId(idToken);
    }

    private OidcProvider getProvider(Provider provider) {
        OidcProvider oidcProvider = authProviderMap.get(provider);

        if (isNull(oidcProvider)) {
            // TODO : 잘못된 인증 제공자
            throw new BusinessException(SecurityExceptionStatus.INTERNAL_AUTHORIZATION_ERROR);
        }

        return oidcProvider;
    }
}
