package com.connect.skilltrade.domain.auth.provider;

import com.connect.skilltrade.domain.auth.dto.OIDCUserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleOIDCProvider implements OIDCProvider {
    private static final String GOOGLE_LOGIN_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String GOOGLE_TOKEN_REQUEST_URI = "https://oauth2.googleapis.com/token";
    private static final String GRANT_TYPE = "authorization_code";
    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    @Value("${oauth.google.client-id}")
    private String clientId;
    @Value("${oauth.google.redirect-uri}")
    private String redirectUri;
    @Value("${oauth.google.client-secret}")
    private String clientSecret;

    @Override
    public String generateLoginLink() {
        return GOOGLE_LOGIN_URL + "?"
                + "client_id=" + clientId + "&"
                + "response_type=" + "code" + "&"
                + "scope=" + "openid%20profile%20email"+"&"
                + "redirect_uri=" + redirectUri;
    }
    @Override
    public OIDCUserInfo getUserInfo(String code) {
        GoogleIdToken idToken = getGoogleIdTokenFromCode(code);
        GoogleIdToken.Payload payload = idToken.getPayload();

        return new OIDCUserInfo(payload.getEmail(), (String) payload.get("name"), payload.getSubject());
    }

    public GoogleIdToken getGoogleIdTokenFromCode(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("redirect_uri", redirectUri);
        formData.add("grant_type", GRANT_TYPE);

        Map<String, Object> response = RestClient.create()
                .post()
                .uri(GOOGLE_TOKEN_REQUEST_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        String idToken = (String) response.get("id_token");

        return verifyIdToken(idToken);
    }

    private GoogleIdToken verifyIdToken(String idToken) {
        try {
            GoogleIdToken verifiedToken = googleIdTokenVerifier.verify(idToken);
            if (verifiedToken == null) {
                throw new IllegalArgumentException("x");
            }
            return verifiedToken;
        } catch (GeneralSecurityException | IOException exception) {
            throw new IllegalArgumentException("xx");
        }
    }

    @Override
    public boolean supports(String socialType) {
        return "GOOGLE".equalsIgnoreCase(socialType);
    }

}
