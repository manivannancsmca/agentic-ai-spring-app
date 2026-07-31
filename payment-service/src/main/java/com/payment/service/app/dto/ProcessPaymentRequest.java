package com.payment.service.app.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessPaymentRequest {
    private Long orderId;
    private BigDecimal amount;
    private String method; // CREDIT_CARD, DEBIT_CARD, PAYPAL
}
