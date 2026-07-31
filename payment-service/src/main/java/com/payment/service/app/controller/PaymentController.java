package com.payment.service.app.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.payment.service.app.dto.ProcessPaymentRequest;
import com.payment.service.app.entity.Payment;
import com.payment.service.app.service.PaymentService;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> processPayment(@RequestBody ProcessPaymentRequest request) {
        log.info("REST: Processing payment for order {}", request.getOrderId());
        Payment payment = paymentService.processPayment(
                request.getOrderId(),
                request.getAmount(),
                request.getMethod()
        );
        HttpStatus status = payment.getStatus() == Payment.PaymentStatus.COMPLETED 
                ? HttpStatus.CREATED : HttpStatus.PAYMENT_REQUIRED;
        return ResponseEntity.status(status).body(payment);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Payment> getPaymentByOrder(@PathVariable Long orderId) {
        log.info("REST: Fetching payment for order {}", orderId);
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        log.info("REST: Fetching payment {}", id);
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping
    public ResponseEntity<List<Payment>> listPayments() {
        log.info("REST: Listing all payments");
        return ResponseEntity.ok(paymentService.listPayments());
    }

    
}