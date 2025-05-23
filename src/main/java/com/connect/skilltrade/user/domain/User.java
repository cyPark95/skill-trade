//package com.connect.skilltrade.user.domain;
//
//import com.connect.skilltrade.common.entity.BaseTimeEntity;
//import com.connect.skilltrade.security.domain.oidc.domain.oauthtype;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import static jakarta.persistence.EnumType.STRING;
//import static jakarta.persistence.GenerationType.IDENTITY;
//import static lombok.AccessLevel.PROTECTED;
//
//@Getter
//@NoArgsConstructor(access = PROTECTED)
//@Entity
//@Table(name = "users")
//public class User extends BaseTimeEntity {
//
//    @Id
//    @GeneratedValue(strategy = IDENTITY)
//    private Long id;
//
//    private String providerId;
//
//    @Enumerated(value = STRING)
//    private oauthtype oAuthType;
//
//    private String email;
//
//    public User(
//            String providerId,
//            oauthtype oAuthType,
//            String email
//    ) {
//        this.providerId = providerId;
//        this.oAuthType = oAuthType;
//        this.email = email;
//    }
//
//    public String getToken() {
//        return this.oAuthType + "_" + this.providerId;
//    }
//}
