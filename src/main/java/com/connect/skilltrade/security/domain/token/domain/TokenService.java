package com.connect.skilltrade.security.domain.token.domain;

import com.connect.skilltrade.security.domain.dto.info.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenGenerator tokenGenerator;

    public TokenInfo generateToken(String userToken) {
        Token token = tokenGenerator.generateToken(userToken);
        return new TokenInfo(token.accessToken(), token.accessTokenExpiredAt(), token.refreshToken());
    }
}
