package com.connect.skilltrade.security.domain;

public interface TokenExecutor {

    Long executeUserId(String accessToken);
}
