package com.example.userservice.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateUser {

    @NotBlank(message = "아이디를 입력해주세요")
    @Size(min = 5, message = "로그인 아이디는 최소 5글자 이상입니다.")
    private String loginId;
    @NotBlank(message = "이메일을 입력해주세요")
    @Email
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 8, message = "비밀번호는 최소 8글자 이상입니다.")
    private String password;
    @NotBlank(message = "비밀번호 확인을 입력해주세요")
    private String passwordCheck;

}
