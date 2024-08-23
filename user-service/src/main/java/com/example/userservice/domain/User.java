package com.example.userservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false,unique = true)
    private String loginId;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false,unique = true)
    private String userUUID;
    @Enumerated(EnumType.STRING)
    private UserType userType;


    @Builder
    public User(Long id, String loginId, String email, String password, String userUUID, UserType userType) {
        this.id = id;
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.userUUID = userUUID;
        this.userType = userType;
    }

    public void changePassword(String changePassword) {
        this.password = changePassword;
    }

}
