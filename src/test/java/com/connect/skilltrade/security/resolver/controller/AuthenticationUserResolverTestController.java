package com.connect.skilltrade.security.resolver.controller;

import com.connect.skilltrade.common.security.resolver.AuthenticationUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationUserResolverTestController {

    @PostMapping("/test/authentication-resolver")
    public Long bindAuthenticationUserId(@AuthenticationUser Long userId) {
        return userId;
    }

//    @PostMapping("/test/authentication-resolver")
//    public Long bindNull(@AuthenticationUser(required = false) Long userId) {
//        return userId;
//    }
}
