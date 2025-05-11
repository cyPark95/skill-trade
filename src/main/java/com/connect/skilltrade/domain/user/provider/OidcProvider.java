package com.connect.skilltrade.domain.user.provider;

public interface OidcProvider {
    String getProviderId(String idToken);
}
