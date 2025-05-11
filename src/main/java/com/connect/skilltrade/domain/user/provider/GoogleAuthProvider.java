package com.connect.skilltrade.domain.user.provider;

import com.connect.skilltrade.common.exception.BusinessException;
import com.connect.skilltrade.security.domain.SecurityExceptionStatus;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class GoogleAuthProvider implements OidcProvider {

    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    @Override
    public String getProviderId(String idToken) {
        return getGoogleIdToken(idToken).getPayload().getSubject();
    }

    private GoogleIdToken getGoogleIdToken(final String idToken) {
        try {
            final GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(idToken);

            if (isNull(googleIdToken)) {
                // TODO
                throw new BusinessException(SecurityExceptionStatus.INTERNAL_AUTHORIZATION_ERROR);
            }

            return googleIdToken;
        } catch (GeneralSecurityException | IOException e) {
            // TODO
            throw new BusinessException(SecurityExceptionStatus.INTERNAL_AUTHORIZATION_ERROR);
        }
    }
}
