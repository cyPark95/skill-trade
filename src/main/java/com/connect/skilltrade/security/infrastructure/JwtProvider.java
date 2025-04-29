package com.connect.skilltrade.security.infrastructure;

import com.connect.skilltrade.common.exception.BusinessException;
import com.connect.skilltrade.security.domain.SecurityExceptionStatus;
import com.connect.skilltrade.security.domain.Token;
import com.connect.skilltrade.security.domain.TokenGenerator;
import com.connect.skilltrade.security.domain.TokenExecutor;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider implements TokenGenerator, TokenExecutor {

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
        this.secretKey = createSecretKey(secret);
        this.accessTokenValidityTime = accessTokenValidityTime;
        this.refreshTokenValidityTime = refreshTokenValidityTime;
    }

    @Override
    public Token generateToken(Long userId) {
        String accessToken = createAccessJwt(userId);
        String refreshToken = createRefreshJwt();

        return new Token(
                accessToken,
                this.accessTokenValidityTime,
                refreshToken
        );
    }

    @Override
    public Long executeUserId(String accessToken) throws BusinessException {
        Claims claims = getClaims(accessToken);
        Long userId = claims.get(TOKEN_CLAIM_KEY, Long.class);

        if(userId == null) {
            throw new BusinessException(SecurityExceptionStatus.ACCESS_TOKEN_CLAIMS_NULL);
        }

        return userId;
    }

    private SecretKey createSecretKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String createAccessJwt(Long id) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + (this.accessTokenValidityTime * MILLISECONDS_TO_SECONDS));

        return Jwts.builder()
                .issuedAt(now)
                .expiration(expiredDate)
                .claim(TOKEN_CLAIM_KEY, id)
                .signWith(this.secretKey)
                .compact();
    }

    private String createRefreshJwt() {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + (this.refreshTokenValidityTime * MILLISECONDS_TO_SECONDS));

        return Jwts.builder()
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(this.secretKey)
                .compact();
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(this.secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new BusinessException(e, SecurityExceptionStatus.EXPIRED_TOKEN, token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            throw new BusinessException(e, SecurityExceptionStatus.INVALID_TOKEN, token);
        }
    }
}
