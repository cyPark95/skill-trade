package com.connect.skilltrade.security.domain;

import com.connect.skilltrade.security.domain.dto.command.GetAuthenticationCommand;
import com.connect.skilltrade.security.domain.dto.info.TokenInfo;
import com.connect.skilltrade.security.domain.oidc.domain.OIDCService;
import com.connect.skilltrade.security.domain.oidc.domain.dto.info.OIDCUserInfo;
import com.connect.skilltrade.security.domain.token.domain.TokenService;
import com.connect.skilltrade.security.domain.user.domain.UserService;
import com.connect.skilltrade.security.domain.user.domain.dto.info.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final OIDCService oidcService;
    private final UserService userService;
    private final TokenService tokenService;

    public TokenInfo getOIDCAuthentication(GetAuthenticationCommand command) {
        OIDCUserInfo oidcUser = oidcService.getOIDCUser(command.toGetOIDCUserCommand());
        UserInfo user = userService.getOrSaveUser(oidcUser.toFindOAuthUserCommand(command.oauthtype()));
        return tokenService.generateToken(user.token());
    }
}
