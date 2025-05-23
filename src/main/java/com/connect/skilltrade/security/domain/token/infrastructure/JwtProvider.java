package com.connect.skilltrade.security.domain.token.infrastructure;

import com.connect.skilltrade.common.exception.BusinessException;
import com.connect.skilltrade.security.domain.SecurityExceptionStatus;
import com.connect.skilltrade.security.domain.token.domain.Token;
import com.connect.skilltrade.security.domain.token.domain.TokenExtractor;
import com.connect.skilltrade.security.domain.token.domain.TokenGenerator;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider implements TokenGenerator, TokenExtractor {

    private static final int SECONDS_TO_MILLISECONDS = 1000;

    private final SecretKey secretKey;
    private final long accessTokenValidityTime;
    private final long refreshTokenValidityTime;

    public JwtProvider(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.access-token-validate-in-seconds}") Long accessTokenValidityTime,
            @Value("${security.jwt.refresh-token-validate-in-seconds}") Long refreshTokenValidityTime
    ) {
        this.secretKey = createSecretKey(secret);
        this.accessTokenValidityTime = accessTokenValidityTime;
        this.refreshTokenValidityTime = refreshTokenValidityTime;
    }

    @Override
    public Token generateToken(String userToken) {
        String accessToken = createAccessJwt(userToken);
        String refreshToken = createRefreshJwt();

        return new Token(
                accessToken,
                this.accessTokenValidityTime,
                refreshToken
        );
    }

    @Override
    public String extractUserToken(String accessToken) throws BusinessException {
        Claims claims = getClaims(accessToken);
        String subject = claims.getSubject();

        if(!StringUtils.hasText(subject)) {
            throw new BusinessException(SecurityExceptionStatus.ACCESS_TOKEN_SUBJECT_NOT_FOUND);
        }

        return subject;
    }

    private SecretKey createSecretKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String createAccessJwt(String token) {
        if(token == null) {
            throw new BusinessException(SecurityExceptionStatus.ACCESS_TOKEN_SUBJECT_NULL_OR_EMPTY);
        }

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + (this.accessTokenValidityTime * SECONDS_TO_MILLISECONDS));

        return Jwts.builder()
                .subject(token)
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(this.secretKey)
                .compact();
    }

    private String createRefreshJwt() {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + (this.refreshTokenValidityTime * SECONDS_TO_MILLISECONDS));

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
