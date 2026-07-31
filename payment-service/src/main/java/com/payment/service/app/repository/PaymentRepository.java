package com.payment.service.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payment.service.app.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
