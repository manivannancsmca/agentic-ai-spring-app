package com.payment.service.app.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payment.service.app.entity.Payment;
import com.payment.service.app.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment processPayment(Long orderId, BigDecimal amount, String method) {
        log.info("Processing payment for order {}: {} via {}", orderId, amount, method);
        
        // Simulate payment gateway logic
        boolean success = simulateGatewayCall();
        
        Payment payment = Payment.builder()
                .orderId(orderId)
                .amount(amount)
                .paymentMethod(method)
                .status(success ? Payment.PaymentStatus.COMPLETED : Payment.PaymentStatus.FAILED)
                .transactionId(success ? "TXN-" + UUID.randomUUID() : null)
                .createdAt(LocalDateTime.now())
                .build();
        
        return paymentRepository.save(payment);
    }

     @Transactional(readOnly = true)
    public Payment getPaymentByOrderId(Long orderId) {
        return (Payment) paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order: " + orderId));
    }

    private boolean simulateGatewayCall() {
        // 90% success rate for simulation
        return Math.random() > 0.1;
    }

    @Transactional(readOnly = true)
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Payment> listPayments() {
        return paymentRepository.findAll();
    }
}
