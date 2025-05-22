package com.connect.skilltrade.security.domain.user.infrastructure;

import com.connect.skilltrade.security.domain.user.domain.AuthenticationUser;
import com.connect.skilltrade.security.domain.user.domain.AuthenticationUserStore;
import com.connect.skilltrade.security.event.SaveUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationUserJpaStore implements AuthenticationUserStore {

    private final AuthenticationUserRepository authenticationUserRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public AuthenticationUser save(AuthenticationUser authenticationUser) {
        authenticationUserRepository.save(authenticationUser);
        SaveUser event = new SaveUser(authenticationUser.getToken(), authenticationUser.getEmail());
        eventPublisher.publishEvent(event);
        return authenticationUser;
    }
}
