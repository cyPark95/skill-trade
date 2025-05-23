package com.connect.skilltrade.security.event;

public record SaveUserEvent(
        String token,
        String email
) {
}
