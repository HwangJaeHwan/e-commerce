package com.example.eventservice.repository.coupon;

import com.example.eventservice.domain.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {


    @Query("select c from Coupon c where c.userUUID =: userUUID and c.used =: false")
    List<Coupon> getCoupons(@Param("userUUID") String userUUID);


}
