package com.connect.skilltrade.security.presentation;

import com.connect.skilltrade.security.domain.SecurityService;
import com.connect.skilltrade.security.presentation.dto.request.OIDCLoginRequest;
import com.connect.skilltrade.security.presentation.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    @PostMapping("/oidc/login")
    public TokenResponse login(@ModelAttribute OIDCLoginRequest request) {
        var response = securityService.getOIDCAuthentication(request.toCommand());
        return new TokenResponse(
                response.accessToken(),
                response.accessTokenExpiredAt(),
                response.refreshToken()
        );
    }
}
