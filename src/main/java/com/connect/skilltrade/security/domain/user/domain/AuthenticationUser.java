package com.connect.skilltrade.security.domain.user.domain;

import com.connect.skilltrade.common.entity.BaseTimeEntity;
import com.connect.skilltrade.security.domain.oidc.domain.OAuthType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "authentication-users")
public class AuthenticationUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String providerId;

    @Enumerated(value = STRING)
    private OAuthType oAuthType;

    private String email;

    public AuthenticationUser(
            String providerId,
            OAuthType oAuthType,
            String email
    ) {
        this.providerId = providerId;
        this.oAuthType = oAuthType;
        this.email = email;
    }

    public String getToken() {
        return this.oAuthType + "_" + this.providerId;
    }
}
