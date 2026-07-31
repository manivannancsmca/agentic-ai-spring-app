package com.payment.service.app.init;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.payment.service.app.entity.Payment;
import com.payment.service.app.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (paymentRepository.count() > 0) {
            log.info("Payment data already exists, skipping initialization");
            return;
        }

        log.info("Seeding default payment data...");

        Payment payment1 = Payment.builder()
                .orderId(1L)
                .amount(new BigDecimal("1299.99"))
                .status(Payment.PaymentStatus.COMPLETED)
                .paymentMethod("CREDIT_CARD")
                .transactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .createdAt(LocalDateTime.now().minusDays(2))
                .build();

        Payment payment2 = Payment.builder()
                .orderId(2L)
                .amount(new BigDecimal("89.97"))
                .status(Payment.PaymentStatus.COMPLETED)
                .paymentMethod("PAYPAL")
                .transactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        Payment payment3 = Payment.builder()
                .orderId(3L)
                .amount(new BigDecimal("119.98"))
                .status(Payment.PaymentStatus.PENDING)
                .paymentMethod("DEBIT_CARD")
                .transactionId(null)
                .createdAt(LocalDateTime.now())
                .build();

        Payment payment4 = Payment.builder()
                .orderId(4L)
                .amount(new BigDecimal("2599.98"))
                .status(Payment.PaymentStatus.FAILED)
                .paymentMethod("CREDIT_CARD")
                .transactionId(null)
                .createdAt(LocalDateTime.now().minusHours(3))
                .build();

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        paymentRepository.save(payment3);
        paymentRepository.save(payment4);

        log.info("Inserted {} sample payments", paymentRepository.count());
    }
}
