package com.connect.skilltrade.security.infrastructure;

import com.connect.skilltrade.common.exception.BusinessException;
import com.connect.skilltrade.security.domain.Role;
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
import java.util.List;

@Slf4j
@Component
public class JwtProvider implements TokenGenerator {

    private static final int SECONDS_TO_MILLISECONDS = 1000;
    private static final String ROLE_CLAIM_KEY = "roles";

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
    public Token generateToken(Long userId, List<Role> roles) {
        String accessToken = makeAccessJwt(userId, roles);
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

    private String makeAccessJwt(Long id, List<Role> roles) {
        if(id == null) {
            throw new BusinessException(SecurityExceptionStatus.ACCESS_TOKEN_SUBJECT_NULL);
        }

        if(roles == null || roles.isEmpty()) {
            throw new BusinessException(SecurityExceptionStatus.ACCESS_TOKEN_ROLE_CLAIM_NULL);
        }

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + (this.accessTokenValidityTime * SECONDS_TO_MILLISECONDS));

        return Jwts.builder()
                .subject(id.toString())
                .issuedAt(now)
                .expiration(expiredDate)
                .claim(ROLE_CLAIM_KEY, roles)
                .signWith(this.secretKey)
                .compact();
    }

    private String makeRefreshJwt() {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + (this.refreshTokenValidityTime * SECONDS_TO_MILLISECONDS));

        return Jwts.builder()
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(this.secretKey)
                .compact();
    }
}
