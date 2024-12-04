package com.example.eventservice.domain;

import com.example.eventservice.request.CouponRequest;
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

    private String name;

    private int percent;

    public Coupon(CouponRequest request) {
        this.userUUID = request.getUserUUID();
        this.name = request.getName();
        this.percent = request.getPercent();

    }
}
