package com.payment.service.app.mcp;

import java.math.BigDecimal;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.payment.service.app.entity.Payment;
import com.payment.service.app.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentMcpTools {

    private final PaymentService paymentService;

    @Tool(name = "process_payment", description = "Process a payment for an order. Returns transaction details.")
    public Payment processPayment(
            @ToolParam(description = "Order ID to charge") Long orderId,
            @ToolParam(description = "Amount to charge") BigDecimal amount,
            @ToolParam(description = "Payment method: CREDIT_CARD, DEBIT_CARD, PAYPAL") String method) {
        log.info("MCP Tool: process_payment({}, {}, {})", orderId, amount, method);
        return paymentService.processPayment(orderId, amount, method);
    }

    @Tool(name = "get_payment_status", description = "Check the payment status for a given order.")
    public Payment.PaymentStatus getPaymentStatus(
            @ToolParam(description = "Order ID") Long orderId) {
        return paymentService.getPaymentByOrderId(orderId).getStatus();
    }
}
