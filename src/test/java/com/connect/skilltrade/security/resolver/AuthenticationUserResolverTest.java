package com.connect.skilltrade.security.resolver;

import com.connect.skilltrade.common.security.resolver.AuthenticationUserResolver;
import com.connect.skilltrade.security.domain.Token;
import com.connect.skilltrade.security.domain.TokenGenerator;
import com.connect.skilltrade.security.filter.TokenAuthenticationFilter;
import com.connect.skilltrade.security.infrastructure.JwtProvider;
import com.connect.skilltrade.security.resolver.controller.AuthenticationUserResolverTestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({TokenAuthenticationFilter.class, JwtProvider.class, AuthenticationUserResolver.class, AuthenticationUserResolverTestController.class})
class AuthenticationUserResolverTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenGenerator tokenGenerator;

    @DisplayName("인증 사용자 정보 바인딩 성공")
    @Test
    void successesAuthentication() throws Exception {
        // given
        Token token = tokenGenerator.generateToken(-1L);

        // when
        // then
        mvc.perform(post("/test/authentication-resolver")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
