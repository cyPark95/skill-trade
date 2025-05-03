package com.connect.skilltrade.security.domain;

public interface TokenGenerator {

    Token generateToken(Long id);
}
