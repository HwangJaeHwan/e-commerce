package com.example.userservice.response;

import com.example.userservice.domain.Address;
import lombok.Getter;



@Getter
public class AddressResponse {

    private Long id;
    private String name;
    private String street;
    private String city;
    private String zipcode;
    private String phoneNumber;


    public AddressResponse(Address address) {
        this.id = address.getId();
        this.name = address.getName();
        this.city = address.getCity();
        this.street = address.getStreet();
        this.zipcode = address.getZipcode();
        this.phoneNumber = address.getPhoneNumber();
    }

}
