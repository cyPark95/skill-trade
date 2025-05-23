package com.connect.skilltrade.security.domain.user.infrastructure;

import com.connect.skilltrade.security.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

//    Optional<User> findBySubjectAndOAuthType(String subject, oauthtype oAuthType);
}
