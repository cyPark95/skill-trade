package com.connect.skilltrade.domain.auth.provider;

import com.connect.skilltrade.domain.auth.dto.OIDCUserInfo;

public interface OIDCProvider {
    String generateLoginLink();
    OIDCUserInfo getUserInfo(String code);
    boolean supports(String provider);
}
