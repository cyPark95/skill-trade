package com.connect.skilltrade.domain.auth;

import com.connect.skilltrade.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(value = STRING)
    private SocialType socialType;

    private String providerId;

    private String nickname;
    private String email;


    @Builder
    public User(
            SocialType socialType,
            String providerId,
            String nickname,
            String email
    ){
        this.socialType = socialType;
        this.providerId = providerId;
        this.nickname = nickname;
        this.email = email;
    }
}
