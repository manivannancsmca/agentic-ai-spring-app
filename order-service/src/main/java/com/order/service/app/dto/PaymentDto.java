package com.order.service.app.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDto {
    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private String status;
    private String transactionId;
}
