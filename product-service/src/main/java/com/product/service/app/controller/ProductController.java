package com.product.service.app.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.product.service.app.dto.CreateProductRequest;
import com.product.service.app.dto.ProductDto;
import com.product.service.app.dto.StockUpdateRequest;
import com.product.service.app.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody CreateProductRequest request) {
        log.info("REST: Creating product {}", request.getName());
        ProductDto product = productService.createProduct(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStockQuantity(),
                request.getCategory()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        log.info("REST: Fetching product {}", id);
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> listProducts() {
        log.info("REST: Listing all products");
        return ResponseEntity.ok(productService.listProducts());
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<ProductDto> updateStock(@PathVariable Long id, 
                                                   @RequestBody StockUpdateRequest request) {
        log.info("REST: Updating stock for product {} to {}", id, request.getQuantity());
        return ResponseEntity.ok(productService.updateStock(id, request.getQuantity()));
    }

    

   
}