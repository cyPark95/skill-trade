package com.connect.skilltrade.security.domain.user.infrastructure;

import com.connect.skilltrade.security.domain.user.domain.User;
import com.connect.skilltrade.security.domain.user.domain.UserReader;
import com.connect.skilltrade.security.domain.user.domain.dto.command.FindOAuthUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserJpaReader implements UserReader {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findUser(FindOAuthUserCommand command) {
//        return userRepository.findBySubjectAndOAuthType(command.subject(), command.oauthtype());
        return null;
    }
}
