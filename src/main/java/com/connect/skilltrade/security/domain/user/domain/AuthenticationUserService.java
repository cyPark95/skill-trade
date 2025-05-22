package com.connect.skilltrade.security.domain.user.domain;

import com.connect.skilltrade.security.domain.user.domain.dto.command.GetAuthenticationUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationUserService {

    private final AuthenticationUserReader authenticationUserReader;
    private final AuthenticationUserStore authenticationUserStore;

    public AuthenticationUser getAuthenticationUser(GetAuthenticationUserCommand command) {
        AuthenticationUser authenticationUser = authenticationUserReader.findAuthenticationUser(command);
        if (authenticationUser == null) {
            return authenticationUserStore.save(command.toDomain());
        }
        return authenticationUser;
    }
}
