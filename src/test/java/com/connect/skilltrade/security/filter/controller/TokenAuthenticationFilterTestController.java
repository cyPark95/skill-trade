package com.connect.skilltrade.security.filter.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenAuthenticationFilterTestController {

    @PostMapping("/test/authentication")
    public void testAuthentication() {
    }
}
