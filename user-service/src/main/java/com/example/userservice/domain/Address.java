package com.example.userservice.domain;

import com.example.userservice.request.AddressRevise;
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

    private String address;

    private String detailAddress;

    private String zipcode;

    private String phoneNumber;

    @ManyToOne(fetch = LAZY)
    private User user;
    @Builder
    public Address(String name, String address, String detailAddress, String zipcode, String phoneNumber, User user) {
        this.name = name;
        this.address = address;
        this.detailAddress = detailAddress;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }

    public void revise(AddressRevise addressRevise) {

        this.name = addressRevise.getName();
        this.address = addressRevise.getAddress();
        this.detailAddress = addressRevise.getDetailAddress();
        this.zipcode = addressRevise.getZipcode();
        this.phoneNumber = addressRevise.getPhoneNumber();
    }
}
