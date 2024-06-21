package com.example.userservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class AddressRequest {

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @NotBlank(message = "도로명을 입력해주세요")
    private String street;

    @NotBlank(message = "도시를 입력해주세요")
    private String city;

    @NotBlank(message = "우편번호를 입력해주세요")
    private String zipcode;

    @NotBlank(message = "휴대폰 번호를 입력해주세요")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "휴대폰 번호가 잘못되었습니다.")
    private String phoneNumber;


}
