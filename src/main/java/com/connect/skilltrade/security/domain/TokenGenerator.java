package com.connect.skilltrade.security.domain;

import java.util.List;

public interface TokenGenerator {

    Token generateToken(Long id, List<Role> roles);
}
