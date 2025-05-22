package com.connect.skilltrade.security.infrastructure;

import com.connect.skilltrade.common.exception.BusinessException;
import com.connect.skilltrade.security.domain.user.domain.Role;
import com.connect.skilltrade.security.domain.SecurityExceptionStatus;
import com.connect.skilltrade.security.domain.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("JWT Provider 검증")
class JwtProviderTest {

    private JwtProvider jwtProvider;

    private static final long USER_ID = -1L;
    private static final List<Role> ROLES = List.of(Role.EXPERT, Role.USER);

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
            Token result = jwtProvider.generateToken(USER_ID, ROLES);

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
            assertThatThrownBy(() -> jwtProvider.generateToken(null, ROLES))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(SecurityExceptionStatus.ACCESS_TOKEN_SUBJECT_NULL_OR_EMPTY.getMessage());
        }

        @DisplayName("실패: 사용자 역할이 null 또는 빈 값")
        @ParameterizedTest
        @NullAndEmptySource
        void rolesIsNull(List<Role> roles) {
            // when
            // then
            assertThatThrownBy(() -> jwtProvider.generateToken(USER_ID, roles))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(SecurityExceptionStatus.ACCESS_TOKEN_ROLE_CLAIM_NULL_OR_EMPTY.getMessage());
        }
    }

    @Nested
    @DisplayName("JWT 사용자 ID 추출")
    class ExtractUserId {

        @DisplayName("성공")
        @Test
        void success() {
            // given
            Token token = jwtProvider.generateToken(USER_ID, ROLES);

            // when
            Long result = jwtProvider.extractUserId(token.accessToken());

            // then
            assertThat(result).isEqualTo(USER_ID);
        }

        @DisplayName("실패: 만료된 엑세스 토큰")
        @Test
        void expiredToken() throws Exception {
            // given
            Token token = jwtProvider.generateToken(USER_ID, ROLES);
            Thread.sleep(1000L);

            // when
            // then
            assertThatThrownBy(() -> jwtProvider.extractUserId(token.accessToken()))
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
            assertThatThrownBy(() -> jwtProvider.extractUserId(invalidToken))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(SecurityExceptionStatus.INVALID_TOKEN.getMessage().formatted(invalidToken));
        }

        @DisplayName("실패: 엑세스 토큰에서 조회한 사용자 ID null")
        @Test
        void userIdNotFound() {
            // given
            Token token = jwtProvider.generateToken(USER_ID, ROLES);

            // when
            // then
            assertThatThrownBy(() -> jwtProvider.extractUserId(token.refreshToken()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(SecurityExceptionStatus.ACCESS_TOKEN_SUBJECT_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("JWT 사용자 역할 추출")
    class ExtractRoles {


        @DisplayName("성공")
        @Test
        void success() {
            // given
            Token token = jwtProvider.generateToken(USER_ID, ROLES);

            // when
            List<Role> result = jwtProvider.extractRoles(token.accessToken());

            // then
            assertThat(result).isEqualTo(ROLES);
        }

        @DisplayName("실패: 엑세스 토큰에서 조회한 사용자 역할 null")
        @Test
        void rolesNotFound() {
            // given
            Token token = jwtProvider.generateToken(USER_ID, ROLES);

            // when
            // then
            assertThatThrownBy(() -> jwtProvider.extractRoles(token.refreshToken()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(SecurityExceptionStatus.ACCESS_TOKEN_ROLE_CLAIM_NOT_FOUND.getMessage());
        }
    }
}
