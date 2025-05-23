package com.connect.skilltrade.security.domain.token.domain;

public interface TokenExtractor {

    String extractUserToken(String accessToken);
}
