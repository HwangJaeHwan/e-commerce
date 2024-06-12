package com.example.userservice.controller;

import com.example.userservice.config.auth.UserSession;
import com.example.userservice.request.AddressRequest;
import com.example.userservice.response.AddressResponse;
import com.example.userservice.service.AddressService;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AddressController {

    private final UserService userService;
    private final AddressService addressService;

    @GetMapping("/addresses")
    public List<AddressResponse> addresses(UserSession userSession) {

        return addressService.addresses(userSession);
    }

    @PostMapping("/address/add")
    public void addAddress(UserSession session, @RequestBody @Valid AddressRequest addressRequest) {

        addressService.addAddress(session, addressRequest);

    }

    @DeleteMapping("/address/delete/{id}")
    public void deleteAddress(UserSession session, @PathVariable Long id) {

        addressService.removeAddress(session, id);

    }
}
