package com.connect.skilltrade.domain.auth.controller;

import com.connect.skilltrade.domain.auth.dto.OIDCUserInfo;
import com.connect.skilltrade.domain.auth.provider.OIDCProvider;
import com.connect.skilltrade.domain.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 소셜 로그인 화면으로 리다이렉트
    @GetMapping("/auth/login")
    public ResponseEntity<?> redirectToSocialLogin(@RequestParam String socialType) {
        OIDCProvider provider = authService.getService(socialType);
        String authorizeUrl = provider.generateLoginLink();
        return ResponseEntity.ok(authorizeUrl);
    }

    // 콜백처리
    @GetMapping("/auth/callback")
    public ResponseEntity<?> callback(@RequestParam String code, @RequestParam String socialType) {
        OIDCUserInfo oidcUserInfo = authService.getUserInfo(code, socialType);
        return ResponseEntity.ok(oidcUserInfo);
    }
}
