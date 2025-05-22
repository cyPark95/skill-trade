package com.connect.skilltrade.domain.auth;

import com.connect.skilltrade.security.domain.user.domain.AuthenticationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<AuthenticationUser,Long> {
    Optional<AuthenticationUser> findByProviderId(String providerId);
}
