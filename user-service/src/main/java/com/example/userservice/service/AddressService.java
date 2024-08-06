package com.example.userservice.service;

import com.example.userservice.config.auth.UserInfo;
import com.example.userservice.domain.Address;
import com.example.userservice.domain.User;
import com.example.userservice.exception.AddressNotFoundException;
import com.example.userservice.exception.UnauthorizedException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.repository.AddressRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.request.AddressRequest;
import com.example.userservice.request.AddressRevise;
import com.example.userservice.response.AddressResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;


    public List<AddressResponse> addresses(UserInfo session) {

        User loginUser = findLoginUser(session);

        return addressRepository.findAllByUser(loginUser).stream().map(AddressResponse::new).toList();
    }

    public void addAddress(UserInfo info, AddressRequest addressRequest) {
        User loginUser = findLoginUser(info);

        addressRepository.save(
                Address.builder()
                        .name(addressRequest.getName())
                        .address(addressRequest.getAddress())
                        .detailAddress(addressRequest.getDetailAddress())
                        .zipcode(addressRequest.getZipcode())
                        .phoneNumber(addressRequest.getPhoneNumber())
                        .user(loginUser)
                        .build());

    }

    public void removeAddress(UserInfo info, Long addressId) {

        User loginUser = findLoginUser(info);

        Address removeAddress = addressRepository.findById(addressId).orElseThrow(AddressNotFoundException::new);

        if (!removeAddress.getUser().equals(loginUser)) {
            throw new UnauthorizedException();
        }

        addressRepository.delete(removeAddress);

    }

    public void reviseAddress(UserInfo info, Long id, AddressRevise addressRevise) {
        Address address = addressRepository.findById(id).orElseThrow(AddressNotFoundException::new);

        if (!address.getUser().getId().equals(info.getId())) {
            throw new UnauthorizedException();
        }

        address.revise(addressRevise);
    }

    private User findLoginUser(UserInfo session) {
        return userRepository.findById(session.getId()).orElseThrow(UserNotFoundException::new);
    }


}
