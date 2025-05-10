package com.connect.skilltrade.security.domain;

import java.util.List;

public interface TokenExtractor {

    Long extractUserId(String accessToken);

    List<Role> extractRoles(String accessToken);
}
