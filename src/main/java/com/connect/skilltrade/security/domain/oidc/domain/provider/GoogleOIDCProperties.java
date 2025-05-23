package com.connect.skilltrade.security.domain.oidc.domain.provider;

import com.connect.skilltrade.security.domain.oidc.domain.dto.command.RequestGoogleIdTokenCommand;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "oauth.oidc.google")
public class GoogleOIDCProperties {

    private String tokenRequestUrl;
    private String grantType;
    private String clientId;
    private String redirectUri;
    private String clientSecret;

    public RequestGoogleIdTokenCommand createRequestIdTokenCommand(String code) {
        return new RequestGoogleIdTokenCommand(code, clientId, clientSecret, redirectUri, grantType);
    }
}
