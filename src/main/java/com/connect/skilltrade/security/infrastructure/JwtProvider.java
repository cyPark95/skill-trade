package com.connect.skilltrade.security.infrastructure;

import com.connect.skilltrade.common.exception.BusinessException;
import com.connect.skilltrade.security.domain.SecurityExceptionStatus;
import com.connect.skilltrade.security.domain.Token;
import com.connect.skilltrade.security.domain.TokenGenerator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtProvider implements TokenGenerator {

    private static final int MILLISECONDS_TO_SECONDS = 1000;
    private static final String TOKEN_CLAIM_KEY = "USER_ID";

    private final SecretKey secretKey;
    private final long accessTokenValidityTime;
    private final long refreshTokenValidityTime;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validate-in-seconds}") Long accessTokenValidityTime,
            @Value("${jwt.refresh-token-validate-in-seconds}") Long refreshTokenValidityTime
    ) {
        this.secretKey = getSecretKey(secret);
        this.accessTokenValidityTime = accessTokenValidityTime;
        this.refreshTokenValidityTime = refreshTokenValidityTime;
    }

    @Override
    public Token generateToken(Long userId) {
        String accessToken = makeAccessJwt(userId);
        String refreshToken = makeRefreshJwt();

        return new Token(
                accessToken,
                this.accessTokenValidityTime,
                refreshToken
        );
    }

    private SecretKey getSecretKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String makeAccessJwt(Long id) {
        if(id == null) {
            throw new BusinessException(SecurityExceptionStatus.ACCESS_TOKEN_CLAIMS_NULL);
        }

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + (this.accessTokenValidityTime * MILLISECONDS_TO_SECONDS));

        Map<String, Long> claims = Map.of(TOKEN_CLAIM_KEY, id);

        return Jwts.builder()
                .issuedAt(now)
                .expiration(expiredDate)
                .claims(claims)
                .signWith(this.secretKey)
                .compact();
    }

    private String makeRefreshJwt() {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + (this.refreshTokenValidityTime * MILLISECONDS_TO_SECONDS));

        return Jwts.builder()
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(secretKey)
                .compact();
    }
}
