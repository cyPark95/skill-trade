package com.connect.skilltrade.domain.user.dto;

import com.connect.skilltrade.domain.user.Provider;

public record UserSignUpRequest(
        Provider provider,
        String idToken,
        String nickname
) {
}
