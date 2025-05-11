package com.connect.skilltrade.domain.user;

import com.connect.skilltrade.domain.user.dto.UserSignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/socialLogin")
    public TokenResponse signUp(@RequestBody UserSignUpRequest request) {
        return userService.signUp(request);
    }
}
