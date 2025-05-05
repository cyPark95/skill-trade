package com.connect.skilltrade.security.infrastructure;

import com.connect.skilltrade.common.exception.BusinessException;
import com.connect.skilltrade.security.domain.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider implements TokenGenerator, TokenExecutor {

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
        this.secretKey = createSecretKey(secret);
        this.accessTokenValidityTime = accessTokenValidityTime;
        this.refreshTokenValidityTime = refreshTokenValidityTime;
    }

    @Override
    public Token generateToken(Long userId, List<Role> roles) {
        String accessToken = createAccessJwt(userId, roles);
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
        String subject = claims.getSubject();

        if(!StringUtils.hasText(subject)) {
            throw new BusinessException(SecurityExceptionStatus.ACCESS_TOKEN_SUBJECT_NOT_FOUND);
        }

        return Long.valueOf(subject);
    }

    @Override
    public List<Role> executeRoles(String accessToken) {
        Claims claims = getClaims(accessToken);
        List<?> roles = claims.get(ROLE_CLAIM_KEY, List.class);

        if (roles == null || roles.isEmpty()) {
            throw new BusinessException(SecurityExceptionStatus.ACCESS_TOKEN_ROLE_CLAIM_NOT_FOUND);
        }

        return roles.stream()
                .map(role -> Role.valueOf(role.toString().toUpperCase()))
                .collect(Collectors.toList());
    }

    private SecretKey createSecretKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String createAccessJwt(Long id, List<Role> roles) {
        if(id == null) {
            throw new BusinessException(SecurityExceptionStatus.ACCESS_TOKEN_SUBJECT_NULL_OR_EMPTY);
        }

        if(roles == null || roles.isEmpty()) {
            throw new BusinessException(SecurityExceptionStatus.ACCESS_TOKEN_ROLE_CLAIM_NULL_OR_EMPTY);
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
