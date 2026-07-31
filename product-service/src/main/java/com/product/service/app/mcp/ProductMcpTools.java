package com.product.service.app.mcp;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.product.service.app.dto.ProductDto;
import com.product.service.app.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductMcpTools {

    private final ProductService productService;

    @Tool(name = "get_product_by_id",
          description = "Retrieve product details by ID including price and stock availability.")
    public ProductDto getProductById(
            @ToolParam(description = "Product unique identifier") Long id) {
        log.info("MCP Tool: get_product_by_id({})", id);
        return productService.getProduct(id);
    }

    @Tool(name = "list_all_products",
          description = "List all available products in the catalog.")
    public List<ProductDto> listAllProducts() {
        return productService.listProducts();
    }

    @Tool(name = "check_product_stock",
          description = "Check if a product is in stock and return available quantity.")
    public Integer checkStock(
            @ToolParam(description = "Product ID to check") Long id) {
        return productService.getProduct(id).getStockQuantity();
    }

     @Tool(name = "create_product",
          description = "Add a new product to the catalog.")
    public ProductDto createProduct(
            @ToolParam(description = "Product name") String name,
            @ToolParam(description = "Description") String description,
            @ToolParam(description = "Price as decimal") BigDecimal price,
            @ToolParam(description = "Initial stock quantity") Integer stockQuantity,
            @ToolParam(description = "Category") String category) {
        return productService.createProduct(name, description, price, stockQuantity, category);
    }
}
