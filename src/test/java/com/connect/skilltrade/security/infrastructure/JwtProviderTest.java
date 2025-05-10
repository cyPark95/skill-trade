package com.connect.skilltrade.security.infrastructure;

import com.connect.skilltrade.common.exception.BusinessException;
import com.connect.skilltrade.security.domain.Role;
import com.connect.skilltrade.security.domain.SecurityExceptionStatus;
import com.connect.skilltrade.security.domain.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    private static final String SECRET = "7YWM7Iqk7Yq4IOy9lOuTnOyXkOyEnCDsgqzsmqntlaAgSldUIOyLnO2BrOumvw==";
    private static final long ACCESS_TOKEN_VALIDITY_TIME = 1L;
    private static final long REFRESH_TOKEN_VALIDITY_TIME = 3L;

    @BeforeEach
    void setUp() {
        this.jwtProvider = new JwtProvider(SECRET, ACCESS_TOKEN_VALIDITY_TIME, REFRESH_TOKEN_VALIDITY_TIME);
    }

    @DisplayName("JWT 생성 성공")
    @Test
    void successGenerateToken() {
        // when
        Token result = jwtProvider.generateToken(USER_ID, ROLES);

        // then
        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isNotNull();
        assertThat(result.refreshToken()).isNotNull();
        assertEquals(ACCESS_TOKEN_VALIDITY_TIME, result.accessTokenExpiredAt());
    }

    @DisplayName("사용자 ID가 null인 경우, JWT 생성 시 예외 발생")
    @Test
    void failGenerateToken_userIdIsNull() {
        // when
        // then
        assertThatThrownBy(() -> jwtProvider.generateToken(null, ROLES))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SecurityExceptionStatus.ACCESS_TOKEN_SUBJECT_NULL.getMessage());
    }

    @DisplayName("사용자 역할이 null 또는 빈 값인 경우, JWT 생성 시 예외 발생")
    @ParameterizedTest
    @NullAndEmptySource
    void failGenerateToken_rolesIsNull(List<Role> roles) {
        // when
        // then
        assertThatThrownBy(() -> jwtProvider.generateToken(USER_ID, roles))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SecurityExceptionStatus.ACCESS_TOKEN_ROLE_CLAIM_NULL.getMessage());
    }
}
