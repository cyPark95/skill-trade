package com.connect.skilltrade.security.infrastructure;

import com.connect.skilltrade.common.exception.BusinessException;
import com.connect.skilltrade.security.domain.SecurityExceptionStatus;
import com.connect.skilltrade.security.domain.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("JWT Provider 검증")
class JwtProviderTest {

    private JwtProvider jwtProvider;

    private static final long USER_ID = -1L;
    private static final String SECRET_KEY = "7YWM7Iqk7Yq4IOy9lOuTnOyXkOyEnCDsgqzsmqntlaAgSldUIOyLnO2BrOumvw==";
    private static final long ACCESS_TOKEN_VALIDITY_TIME = 1L;
    private static final long REFRESH_TOKEN_VALIDITY_TIME = 3L;

    @BeforeEach
    void setUp() {
        this.jwtProvider = new JwtProvider(SECRET_KEY, ACCESS_TOKEN_VALIDITY_TIME, REFRESH_TOKEN_VALIDITY_TIME);
    }

    @DisplayName("JWT 생성 성공")
    @Test
    void successGenerateToken() {
        // when
        Token result = jwtProvider.generateToken(USER_ID);

        // then
        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isNotNull();
        assertThat(result.refreshToken()).isNotNull();
        assertEquals(ACCESS_TOKEN_VALIDITY_TIME, result.accessTokenExpiredAt());
    }

    @DisplayName("엑세스 토큰에서 사용자 ID 정보 조회 성공")
    @Test
    void successExecuteUserId() {
        // given
        Token token = jwtProvider.generateToken(USER_ID);

        // when
        Long result = jwtProvider.executeUserId(token.accessToken());

        // then
        assertThat(result).isEqualTo(USER_ID);
    }

    @DisplayName("만료된 엑세스 토큰으로 사용자 ID 조회 시, 예외 발생")
    @Test
    void failExecuteUserId_expiredToken() throws Exception {
        // given
        Token token = jwtProvider.generateToken(USER_ID);
        Thread.sleep(1000L);

        // when
        // then
        assertThatThrownBy(() -> jwtProvider.executeUserId(token.accessToken()))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SecurityExceptionStatus.EXPIRED_TOKEN.getMessage().formatted(token.accessToken()));
    }

    @DisplayName("잘못된 형식의 엑세스 토근으로 사용자 ID 조회 시, 예외 발생")
    @Test
    void failExecuteUserId_invalidToken() {
        // given
        String invalidToken = "INVALID_ACCESS_TOKEN";

        // when
        // then
        assertThatThrownBy(() -> jwtProvider.executeUserId(invalidToken))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SecurityExceptionStatus.INVALID_TOKEN.getMessage().formatted(invalidToken));
    }

    @DisplayName("엑세스 토큰에서 조회한 사용자 ID가 null인 경우, 예외 발생")
    @Test
    void failExecuteUserId_invalidArgument() {
        // given
        Token token = jwtProvider.generateToken(null);

        // when
        // then
        assertThatThrownBy(() -> jwtProvider.executeUserId(token.accessToken()))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SecurityExceptionStatus.ACCESS_TOKEN_CLAIMS_NULL.getMessage());
    }
}
