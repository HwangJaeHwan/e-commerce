package com.example.userservice.response;

import com.example.userservice.domain.Address;
import lombok.Getter;



@Getter
public class AddressResponse {

    private Long id;
    private String name;
    private String address;
    private String detailAddress;
    private String zipcode;
    private String phoneNumber;


    public AddressResponse(Address address) {
        this.id = address.getId();
        this.name = address.getName();
        this.address = address.getAddress();
        this.detailAddress = address.getDetailAddress();
        this.zipcode = address.getZipcode();
        this.phoneNumber = address.getPhoneNumber();
    }

}
