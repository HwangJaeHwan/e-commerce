package com.example.eventservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String userUUID;


    public Coupon(String userUUID) {
        this.userUUID = userUUID;
    }
}
