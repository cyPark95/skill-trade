package com.connect.skilltrade.security.event;

public record SaveUser(
        String token,
        String email
) {
}
