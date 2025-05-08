package com.connect.skilltrade.security.filter.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenAuthorizationFilterTestController {

    @PostMapping("/test/authorization")
    public void checkAuthorization() {
    }
}
