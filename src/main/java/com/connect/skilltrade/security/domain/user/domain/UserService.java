package com.connect.skilltrade.security.domain.user.domain;

import com.connect.skilltrade.security.domain.user.domain.dto.command.FindOAuthUserCommand;
import com.connect.skilltrade.security.domain.user.domain.dto.info.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserReader userReader;
    private final UserStore userStore;

    @Transactional
    public UserInfo getOrSaveUser(FindOAuthUserCommand command) {
        User user = userReader.findUser(command)
                .orElseGet(() -> userStore.save(command.toDomain()));
        return new UserInfo(user.getToken(), user.getEmail(), user.getRoles());
    }
}
