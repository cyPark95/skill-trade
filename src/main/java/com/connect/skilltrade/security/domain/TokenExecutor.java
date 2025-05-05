package com.connect.skilltrade.security.domain;

import java.util.List;

public interface TokenExecutor {

    Long executeUserId(String accessToken);

    List<Role> executeRoles(String accessToken);
}
