package com.connect.skilltrade.security.domain.user.domain;

import com.connect.skilltrade.common.entity.BaseTimeEntity;
import com.connect.skilltrade.security.domain.oidc.domain.OAuthType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "authentication-users")
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String subject;

    @Enumerated(EnumType.STRING)
    private OAuthType oauthType;

    private String email;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Set<Role> roles;

    public User(
            String subject,
            OAuthType oauthType,
            String email,
            Role authority
    ) {
        this.subject = subject;
        this.oauthType = oauthType;
        this.email = email;
        this.roles = Set.of(authority);
    }

    public static User createInitUser(
            String subject,
            OAuthType oAuthType,
            String email
    ) {
        Role authority = new Role(RoleType.USER);
        return new User(subject, oAuthType, email, authority);
    }

    public String getToken() {
        return this.oauthType + "_" + this.subject;
    }

    public List<RoleType> getRoles() {
        return roles.stream()
                .map(Role::getRoleType)
                .toList();
    }
}
