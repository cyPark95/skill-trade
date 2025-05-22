package com.connect.skilltrade.security.domain;

import com.connect.skilltrade.security.domain.user.domain.Role;

import java.util.List;

public interface TokenGenerator {

    Token generateToken(Long id, List<Role> roles);
}
