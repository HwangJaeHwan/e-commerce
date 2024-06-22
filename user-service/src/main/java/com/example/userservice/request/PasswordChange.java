package com.example.userservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PasswordChange {

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 8, message = "비밀번호는 최소 8글자 이상입니다.")
    private String password;
    @NotBlank(message = "바꿀 비밀번호를 입력해주세요)")
    private String changePassword;
    @NotBlank(message = "비밀번호 확인을 입력해주세요)")
    private String changePasswordCheck;
}
