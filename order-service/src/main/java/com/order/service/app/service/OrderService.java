package com.order.service.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.order.service.app.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final RestTemplate restTemplate;

    @Value("${service.urls.user}")
    private String userServiceUrl;
    
    @Value("${service.urls.product}")
    private String productServiceUrl;
    
    @Value("${service.urls.payment}")
    private String paymentServiceUrl;
    
    @Value("${service.urls.notification}")
    private String notificationServiceUrl;

    

}
