package com.connect.skilltrade.security.domain.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {

    USER("일반 사용자"),
    EXPERT("전문가"),
    ADMIN("관리자"),
    ;

    private final String description;
}
