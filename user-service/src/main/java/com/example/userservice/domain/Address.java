package com.example.userservice.domain;

import jakarta.persistence.*;
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

    private String name;

    private String street;

    private String city;

    private String zipcode;

    private String phoneNumber;

    @ManyToOne(fetch = LAZY)
    private User user;
    @Builder
    public Address(String name, String street, String city, String zipcode, String phoneNumber, User user) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }
}
