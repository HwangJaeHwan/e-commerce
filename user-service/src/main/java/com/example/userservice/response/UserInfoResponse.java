package com.example.userservice.response;

import com.example.userservice.domain.User;
import com.example.userservice.domain.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {

    private Long id;
    private String loginId;
    private String userUUID;
    private String email;
    private UserType userType;


    public UserInfoResponse(User user) {
        this.id = user.getId();
        this.loginId = user.getLoginId();
        this.userUUID = user.getUserUUID();
        this.email = user.getEmail();
        this.userType = user.getUserType();

    }
}
