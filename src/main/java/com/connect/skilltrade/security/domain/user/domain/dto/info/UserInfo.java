package com.connect.skilltrade.security.domain.user.domain.dto.info;

import com.connect.skilltrade.security.domain.user.domain.RoleType;

import java.util.List;

public record UserInfo(
        String token,
        String email,
        List<RoleType> roles
) {
}
