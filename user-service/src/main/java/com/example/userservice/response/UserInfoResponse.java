package com.example.userservice.response;

import com.example.userservice.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {

    private String loginId;
    private String email;


    public UserInfoResponse(User user) {
        this.loginId = user.getLoginId();
        this.email = user.getEmail();

    }
}
