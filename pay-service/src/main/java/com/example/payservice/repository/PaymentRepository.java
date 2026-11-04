package com.example.payservice.repository;

import com.example.payservice.domain.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentDetail,Long> {

    Optional<PaymentDetail> findByImpUid(String impUid);

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select p from PaymentDetail p where p.impUid = :impUid")
    Optional<PaymentDetail> findForRefund(@org.springframework.data.repository.query.Param("impUid") String impUid);
}
