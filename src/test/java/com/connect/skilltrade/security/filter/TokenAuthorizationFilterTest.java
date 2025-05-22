package com.connect.skilltrade.security.filter;

import com.connect.skilltrade.security.domain.user.domain.Role;
import com.connect.skilltrade.security.domain.SecurityExceptionStatus;
import com.connect.skilltrade.security.domain.Token;
import com.connect.skilltrade.security.domain.TokenGenerator;
import com.connect.skilltrade.security.filter.controller.TokenAuthorizationFilterTestController;
import com.connect.skilltrade.security.infrastructure.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Token 인가 필터 검증")
@WebMvcTest({TokenAuthorizationFilter.class, JwtProvider.class, TokenAuthorizationFilterTestController.class})
class TokenAuthorizationFilterTest {

    private static final long USER_ID = -1L;
    private static final List<Role> ROLES = List.of(Role.EXPERT, Role.USER);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenGenerator tokenGenerator;
    
    @Value("${security.grant-type}")
    private String grantType;

    @DisplayName("Token 인가 성공")
    @Test
    void successesAuthentication() throws Exception {
        // given
        Token token = tokenGenerator.generateToken(USER_ID, ROLES);

        // when
        // then
        mvc.perform(post("/test/authorization")
                        .header(HttpHeaders.AUTHORIZATION, grantType + " " + token.accessToken()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("HTTP 헤더에 인가 정보 없는 요청인 경우 인가 실패")
    @Test
    void failAuthentication_emptyAuthorization() throws Exception {
        // when
        // then
        mvc.perform(post("/test/authorization"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(SecurityExceptionStatus.INVALID_AUTHORIZATION.getCode()));
    }

    @DisplayName("다른 Grant Type으로 시작하는 인가 정보로 요청한 경우 인가 실패")
    @Test
    void failAuthentication_emptyPrefix() throws Exception {
        // when
        // then
        mvc.perform(post("/test/authorization")
                        .header(HttpHeaders.AUTHORIZATION, "accessToken"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(SecurityExceptionStatus.INVALID_AUTHORIZATION.getCode()));
    }

    @DisplayName("인가 정보에 토큰 없이 요청한 경우 인가 실패")
    @Test
    void failAuthentication_emptyToken() throws Exception {
        // when
        // then
        mvc.perform(post("/test/authorization")
                        .header(HttpHeaders.AUTHORIZATION, grantType))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(SecurityExceptionStatus.INVALID_AUTHORIZATION.getCode()));
    }

    @DisplayName("만료된 토큰 요청인 경우 인가 실패")
    @Test
    void failAuthentication_expiredToken() throws Exception {
        // given
        Token token = tokenGenerator.generateToken(USER_ID, ROLES);
        Thread.sleep(2000);

        // when
        // then
        mvc.perform(post("/test/authorization")
                        .header(HttpHeaders.AUTHORIZATION, grantType + " " + token.accessToken()))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(SecurityExceptionStatus.EXPIRED_TOKEN.getCode()));
    }

    @DisplayName("유효하지 않은 토큰 요청인 경우 인가 실패")
    @Test
    void failAuthentication_invalidToken() throws Exception {
        // when
        // then
        mvc.perform(post("/test/authorization")
                        .header(HttpHeaders.AUTHORIZATION, grantType + " " + "accessToken"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(SecurityExceptionStatus.INVALID_TOKEN.getCode()));
    }
}
