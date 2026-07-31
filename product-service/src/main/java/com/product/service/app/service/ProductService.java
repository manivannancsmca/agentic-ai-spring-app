package com.product.service.app.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.service.app.dto.ProductDto;
import com.product.service.app.entity.Product;
import com.product.service.app.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductDto getProduct(Long id) {
        return productRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<ProductDto> listProducts() {
        return productRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductDto createProduct(String name, String description, BigDecimal price, Integer stock, String category) {
        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .stockQuantity(stock)
                .category(category)
                .build();
        return toDto(productRepository.save(product));
    }

    @Transactional
    public ProductDto updateStock(Long id, Integer quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        product.setStockQuantity(quantity);
        return toDto(productRepository.save(product));
    }
    
    private ProductDto toDto(Product p) {
        return ProductDto.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .stockQuantity(p.getStockQuantity())
                .category(p.getCategory())
                .build();
    }
}
