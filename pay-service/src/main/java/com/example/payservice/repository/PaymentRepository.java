package com.example.payservice.repository;

import com.example.payservice.domain.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentDetail,Long> {

    Optional<PaymentDetail> findByImpUid(String impUid);
}
