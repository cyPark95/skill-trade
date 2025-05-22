package com.connect.skilltrade.security.domain.user.domain;

import com.connect.skilltrade.security.domain.user.domain.dto.command.GetAuthenticationUserCommand;

public interface AuthenticationUserReader {

    AuthenticationUser findAuthenticationUser(GetAuthenticationUserCommand command);
}
