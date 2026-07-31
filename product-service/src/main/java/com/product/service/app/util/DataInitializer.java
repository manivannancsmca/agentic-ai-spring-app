package com.product.service.app.util;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.product.service.app.service.ProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ProductService productService;
    
    @Override
    public void run(String... args) {
        if (productService.listProducts().isEmpty()) {
            productService.createProduct("Laptop Pro", "High-performance laptop", 
                new BigDecimal("1299.99"), 50, "Electronics");
            productService.createProduct("Wireless Mouse", "Ergonomic wireless mouse", 
                new BigDecimal("29.99"), 200, "Accessories");
            productService.createProduct("USB-C Hub", "7-in-1 USB-C hub", 
                new BigDecimal("59.99"), 100, "Accessories");
        }
    }
}
