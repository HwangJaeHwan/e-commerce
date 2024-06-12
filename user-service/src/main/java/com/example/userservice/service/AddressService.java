package com.example.userservice.service;

import com.example.userservice.config.auth.UserSession;
import com.example.userservice.domain.Address;
import com.example.userservice.domain.User;
import com.example.userservice.repository.AddressRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.request.AddressRequest;
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


    public List<AddressResponse> addresses(UserSession session) {

        User loginUser = findLoginUser(session);

        return addressRepository.findAllByUser(loginUser).stream().map(AddressResponse::new).toList();
    }

    public void addAddress(UserSession session, AddressRequest addressRequest) {
        User loginUser = findLoginUser(session);

        addressRepository.save(
                Address.builder()
                        .name(addressRequest.getName())
                        .address(addressRequest.getAddress())
                        .phoneNumber(addressRequest.getPhoneNumber())
                        .user(loginUser)
                        .build());

    }

    public void removeAddress(UserSession session, Long addressId) {

        User loginUser = findLoginUser(session);

        Address removeAddress = addressRepository.findById(addressId).orElseThrow(() -> new RuntimeException("주소록 없음"));

        if (!removeAddress.getUser().equals(loginUser)) {
            throw new RuntimeException("삭제 권한 없음");
        }

        addressRepository.delete(removeAddress);

    }

    private User findLoginUser(UserSession session) {
        return userRepository.findById(session.getId()).orElseThrow(RuntimeException::new);
    }


}
