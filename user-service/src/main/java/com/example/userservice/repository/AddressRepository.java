package com.example.userservice.repository;

import com.example.userservice.domain.Address;
import com.example.userservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository  extends JpaRepository<Address, Long> {

    List<Address> findAllByUser(User user);

}
