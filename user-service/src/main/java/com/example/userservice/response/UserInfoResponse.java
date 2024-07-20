package com.example.userservice.response;

import com.example.userservice.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {

    private Long id;
    private String loginId;
    private String userUUID;
    private String email;


    public UserInfoResponse(User user) {
        this.id = user.getId();
        this.loginId = user.getLoginId();
        this.userUUID = user.getUserUUID();
        this.email = user.getEmail();

    }
}
