package com.example.userservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Address {

    @Id
    @GeneratedValue
    @Column(name = "address_id")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String street;

    @NotNull
    private String city;

    @NotNull
    private String zipcode;

    @NotNull
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "휴대폰 번호가 잘못되었습니다.")
    private String phoneNumber;

    @ManyToOne(fetch = LAZY)
    private User user;


}
