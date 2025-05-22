package com.connect.skilltrade.security.domain.oidc.domain.provider;

import com.connect.skilltrade.common.exception.BusinessException;
import com.connect.skilltrade.security.domain.SecurityExceptionStatus;
import com.connect.skilltrade.security.domain.oidc.domain.OAuthType;
import com.connect.skilltrade.security.domain.oidc.domain.dto.command.RequestGoogleIdTokenCommand;
import com.connect.skilltrade.security.domain.oidc.domain.dto.info.GoogleTokenInfo;
import com.connect.skilltrade.security.domain.oidc.domain.dto.info.OIDCUserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleOIDCProvider implements OIDCProvider {

    private static final String BASIC_APPROVAL_CODE = "code";
    private static final List<String> SCOPE_ARGUMENTS = List.of("openid", "email");
    private static final String NAME_PAYLOAD_KEY = "name";

    private final RestClient restClient;
    private final GoogleOIDCProperties googleOIDCProperties;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public GoogleOIDCProvider(GoogleOIDCProperties properties, RestClient restClient) {
        this.restClient = restClient;
        this.googleOIDCProperties = properties;
        this.googleIdTokenVerifier = new GoogleIdTokenVerifier
                .Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(properties.getClientId()))
                .build();
    }

    @Override
    public boolean supports(OAuthType OAuthType) {
        return OAuthType.GOOGLE == OAuthType;
    }

    @Override
    public String getAuthorizationUrl() {
        return UriComponentsBuilder.fromUriString(googleOIDCProperties.getLoginUrl())
                .queryParam("response_type", BASIC_APPROVAL_CODE)
                .queryParam("client_id", googleOIDCProperties.getClientId())
                .queryParam("scope", SCOPE_ARGUMENTS)
                .queryParam("redirect_uri", googleOIDCProperties.getRedirectUri())
                .build()
                .toUriString();
    }

    @Override
    public OIDCUserInfo getOIDCUserInfo(String code) throws BusinessException {
        GoogleTokenInfo tokenInfo = exchangeIdTokenAndAccessToken(code);
        GoogleIdToken googleIdToken = verifyIdToken(tokenInfo.idToken());
        Payload payload = googleIdToken.getPayload();

        return new OIDCUserInfo(
                payload.getSubject(),
                payload.getEmail(),
                (String) payload.get(NAME_PAYLOAD_KEY)
        );
    }

    private GoogleTokenInfo exchangeIdTokenAndAccessToken(String code) {
        RequestGoogleIdTokenCommand command = googleOIDCProperties.createRequestIdTokenCommand(code);
        return restClient
                .post()
                .uri(googleOIDCProperties.getTokenRequestUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(command)
                .retrieve()
                .onStatus(
                        status -> !status.is2xxSuccessful(),
                        (request, response) -> {
                            throw new BusinessException(SecurityExceptionStatus.FAIL_EXCHANGE_GOOGLE_ID_TOKEN, code);
                        })
                .body(GoogleTokenInfo.class);
    }

    private GoogleIdToken verifyIdToken(String idToken) {
        try {
            GoogleIdToken verifiedToken = googleIdTokenVerifier.verify(idToken);
            if (verifiedToken == null) {
                throw new BusinessException(SecurityExceptionStatus.GOOGLE_ID_TOKEN_PAYLOAD_NOT_FOUND, idToken);
            }

            return verifiedToken;
        } catch (GeneralSecurityException | IOException e) {
            throw new BusinessException(e, SecurityExceptionStatus.INVALID_GOOGLE_ID_TOKEN, idToken);
        }
    }
}
