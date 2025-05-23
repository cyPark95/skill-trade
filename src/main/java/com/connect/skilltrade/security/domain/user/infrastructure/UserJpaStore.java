package com.connect.skilltrade.security.domain.user.infrastructure;

import com.connect.skilltrade.security.domain.user.domain.User;
import com.connect.skilltrade.security.domain.user.domain.UserStore;
import com.connect.skilltrade.security.event.SaveUserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserJpaStore implements UserStore {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public User save(User user) {
        userRepository.save(user);
        SaveUserEvent event = new SaveUserEvent(user.getToken(), user.getEmail());
        eventPublisher.publishEvent(event);
        return user;
    }
}
