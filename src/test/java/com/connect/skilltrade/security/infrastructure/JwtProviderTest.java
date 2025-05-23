package com.connect.skilltrade.security.infrastructure;

import com.connect.skilltrade.common.exception.BusinessException;
import com.connect.skilltrade.security.domain.SecurityExceptionStatus;
import com.connect.skilltrade.security.domain.token.domain.Token;
import com.connect.skilltrade.security.domain.token.infrastructure.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("JWT Provider 검증")
class JwtProviderTest {

    private JwtProvider jwtProvider;

    private static final String USER_TOKEN = "OAuth Token";

    private static final String SECRET_KEY = "7YWM7Iqk7Yq4IOy9lOuTnOyXkOyEnCDsgqzsmqntlaAgSldUIOyLnO2BrOumvw==";
    private static final long ACCESS_TOKEN_VALIDITY_TIME = 1L;
    private static final long REFRESH_TOKEN_VALIDITY_TIME = 3L;

    @BeforeEach
    void setUp() {
        this.jwtProvider = new JwtProvider(SECRET_KEY, ACCESS_TOKEN_VALIDITY_TIME, REFRESH_TOKEN_VALIDITY_TIME);
    }

    @Nested
    @DisplayName("JWT 생성")
    class GenerateToken {

        @DisplayName("성공")
        @Test
        void success() {
            // when
            Token result = jwtProvider.generateToken(USER_TOKEN);

            // then
            assertThat(result).isNotNull();
            assertThat(result.accessToken()).isNotNull();
            assertThat(result.refreshToken()).isNotNull();
            assertEquals(ACCESS_TOKEN_VALIDITY_TIME, result.accessTokenExpiredAt());
        }

        @DisplayName("실패: 사용자 ID가 null")
        @Test
        void userIdIsNull() {
            // when
            // then
            assertThatThrownBy(() -> jwtProvider.generateToken(null))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(SecurityExceptionStatus.ACCESS_TOKEN_SUBJECT_NULL_OR_EMPTY.getMessage());
        }
    }

    @Nested
    @DisplayName("JWT 사용자 ID 추출")
    class ExtractUserId {

        @DisplayName("성공")
        @Test
        void success() {
            // given
            Token token = jwtProvider.generateToken(USER_TOKEN);

            // when
            String result = jwtProvider.extractUserToken(token.accessToken());

            // then
            assertThat(result).isEqualTo(USER_TOKEN);
        }

        @DisplayName("실패: 만료된 엑세스 토큰")
        @Test
        void expiredToken() throws Exception {
            // given
            Token token = jwtProvider.generateToken(USER_TOKEN);
            Thread.sleep(1000L);

            // when
            // then
            assertThatThrownBy(() -> jwtProvider.extractUserToken(token.accessToken()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(SecurityExceptionStatus.EXPIRED_TOKEN.getMessage().formatted(token.accessToken()));
        }

        @DisplayName("실패: 잘못된 형식의 엑세스 토큰")
        @Test
        void invalidToken() {
            // given
            String invalidToken = "INVALID_ACCESS_TOKEN";

            // when
            // then
            assertThatThrownBy(() -> jwtProvider.extractUserToken(invalidToken))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(SecurityExceptionStatus.INVALID_TOKEN.getMessage().formatted(invalidToken));
        }

        @DisplayName("실패: 엑세스 토큰에서 조회한 사용자 ID null")
        @Test
        void userIdNotFound() {
            // given
            Token token = jwtProvider.generateToken(USER_TOKEN);

            // when
            // then
            assertThatThrownBy(() -> jwtProvider.extractUserToken(token.refreshToken()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(SecurityExceptionStatus.ACCESS_TOKEN_SUBJECT_NOT_FOUND.getMessage());
        }
    }
}
