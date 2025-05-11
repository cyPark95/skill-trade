package com.connect.skilltrade.domain.user;

import com.connect.skilltrade.security.domain.Token;

public record TokenResponse(String accessToken, String refreshToken) {

    public static TokenResponse from(Token token){
        return new TokenResponse(token.accessToken(), token.refreshToken());
    }
}
