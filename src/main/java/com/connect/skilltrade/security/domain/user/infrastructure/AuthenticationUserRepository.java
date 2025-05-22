package com.connect.skilltrade.security.domain.user.infrastructure;

import com.connect.skilltrade.security.domain.user.domain.AuthenticationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationUserRepository extends JpaRepository<AuthenticationUser, Long> {
}
