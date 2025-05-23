package com.connect.skilltrade.security.domain.token.domain;

public interface TokenGenerator {

    Token generateToken(String token);
}
