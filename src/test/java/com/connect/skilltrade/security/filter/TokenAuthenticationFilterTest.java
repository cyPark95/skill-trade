package com.connect.skilltrade.security.filter;

import com.connect.skilltrade.security.domain.Role;
import com.connect.skilltrade.security.domain.SecurityExceptionStatus;
import com.connect.skilltrade.security.domain.Token;
import com.connect.skilltrade.security.domain.TokenGenerator;
import com.connect.skilltrade.security.filter.controller.TokenAuthenticationFilterTestController;
import com.connect.skilltrade.security.infrastructure.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({TokenAuthenticationFilter.class, JwtProvider.class, TokenAuthenticationFilterTestController.class})
class TokenAuthenticationFilterTest {

    private static final long USER_ID = -1L;
    private static final List<Role> ROLES = List.of(Role.EXPERT, Role.USER);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenGenerator tokenGenerator;

    @DisplayName("Token 인증 성공")
    @Test
    void successesAuthentication() throws Exception {
        // given
        Token token = tokenGenerator.generateToken(USER_ID, ROLES);

        // when
        // then
        mvc.perform(post("/test/authentication")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("HTTP 헤더에 인증 정보 없는 요청인 경우 인증 실패")
    @Test
    void failAuthentication_emptyAuthorization() throws Exception {
        // when
        // then
        mvc.perform(post("/test/authentication"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(SecurityExceptionStatus.INVALID_AUTHORIZATION.getCode()));
    }

    @DisplayName("Bearer로 시작하지 않은 인증 정보로 요청한 경우 인증 실패")
    @Test
    void failAuthentication_emptyPrefix() throws Exception {
        // when
        // then
        mvc.perform(post("/test/authentication")
                        .header(HttpHeaders.AUTHORIZATION, "accessToken"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(SecurityExceptionStatus.INVALID_AUTHORIZATION.getCode()));
    }

    @DisplayName("인증 정보에 토큰 없이 요청한 경우 인증 실패")
    @Test
    void failAuthentication_emptyToken() throws Exception {
        // when
        // then
        mvc.perform(post("/test/authentication")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(SecurityExceptionStatus.INVALID_AUTHORIZATION.getCode()));
    }

    @DisplayName("만료된 토큰 요청인 경우 인증 실패")
    @Test
    void failAuthentication_expiredToken() throws Exception {
        // given
        Token token = tokenGenerator.generateToken(USER_ID, ROLES);
        Thread.sleep(2000);

        // when
        // then
        mvc.perform(post("/test/authentication")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken()))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(SecurityExceptionStatus.EXPIRED_TOKEN.getCode()));
    }

    @DisplayName("유효하지 않은 토큰 요청인 경우 인증 실패")
    @Test
    void failAuthentication_invalidToken() throws Exception {
        // when
        // then
        mvc.perform(post("/test/authentication")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "accessToken"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(SecurityExceptionStatus.INVALID_TOKEN.getCode()));
    }
}
