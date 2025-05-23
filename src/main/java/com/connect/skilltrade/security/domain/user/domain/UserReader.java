package com.connect.skilltrade.security.domain.user.domain;

import com.connect.skilltrade.security.domain.user.domain.dto.command.FindOAuthUserCommand;

import java.util.Optional;

public interface UserReader {

    Optional<User> findUser(FindOAuthUserCommand command);
}
