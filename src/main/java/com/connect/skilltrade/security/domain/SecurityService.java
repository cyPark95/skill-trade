package com.connect.skilltrade.security.domain;

import com.connect.skilltrade.security.domain.dto.command.GetLoginUserCommand;
import com.connect.skilltrade.security.domain.oidc.domain.OIDCService;
import com.connect.skilltrade.security.domain.oidc.domain.dto.info.OIDCUserInfo;
import com.connect.skilltrade.security.domain.user.domain.AuthenticationUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final OIDCService oidcService;
    private final AuthenticationUserService authenticationUserService;

    public void login(GetLoginUserCommand command) {
        OIDCUserInfo oidcUser = oidcService.getOIDCUser(command.toGetOIDCUserCommand());

    }
}
