package com.example.userservice.controller;

import com.example.userservice.config.auth.UserInfo;
import com.example.userservice.request.AddressRequest;
import com.example.userservice.request.AddressRevise;
import com.example.userservice.response.AddressResponse;
import com.example.userservice.service.AddressService;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-service")
@RequiredArgsConstructor
public class AddressController {

    private final UserService userService;
    private final AddressService addressService;

    @GetMapping("/addresses")
    public List<AddressResponse> addresses(UserInfo userInfo) {

        return addressService.addresses(userInfo);
    }

    @PostMapping("/address/add")
    public void addAddress(UserInfo info, @RequestBody @Valid AddressRequest addressRequest) {

        addressService.addAddress(info, addressRequest);

    }

    @PatchMapping("/address/revise/{id}")
    public void reviseAddress(UserInfo info, @PathVariable Long id, @RequestBody @Valid AddressRevise addressRevise) {
        addressService.reviseAddress(info, id, addressRevise);
    }

    @DeleteMapping("/address/delete/{id}")
    public void deleteAddress(UserInfo info, @PathVariable Long id) {

        addressService.removeAddress(info, id);

    }
}
