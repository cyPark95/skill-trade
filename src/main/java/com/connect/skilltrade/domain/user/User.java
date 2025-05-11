package com.connect.skilltrade.domain.user;

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
    private Provider provider;

    private String providerId;

    private String nickname;


    @Builder
    public User(
            Provider provider,
            String providerId,
            String nickname
    ){
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
    }
}
