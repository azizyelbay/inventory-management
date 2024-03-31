package com.oneamz.inventorymanagement.controller;

import com.oneamz.inventorymanagement.dto.CheckStockResponse;
import com.oneamz.inventorymanagement.dto.ProductDto;
import com.oneamz.inventorymanagement.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {
        log.debug("Getting all products");
        List<ProductDto> products = productService.getAllProducts();
        log.debug("Retrieved {} products", products.size());
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<ProductDto> saveProduct(@RequestBody ProductDto request) {
        log.debug("Saving product: {}", request);
        ProductDto savedProduct = productService.saveProduct(request);
        log.debug("Product saved: {}", savedProduct);
        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        log.debug("Getting product with id: {}", productId);
        ProductDto product = productService.getProductById(productId);
        log.debug("Retrieved product: {}", product);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId,
                                                    @RequestBody ProductDto request) {
        log.debug("Updating product with id {}: {}", productId, request);
        ProductDto updatedProduct = productService.updateProduct(productId, request);
        log.debug("Product updated: {}", updatedProduct);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        log.debug("Deleting product with id: {}", productId);
        productService.deleteProduct(productId);
        log.debug("Product deleted successfully");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-stock")
    public ResponseEntity<List<CheckStockResponse>> checkStock(@RequestParam List<Long> productId) {
        log.debug("Checking stock for product with code: {}", productId);
        List<CheckStockResponse> response = productService.checkStock(productId);
        log.debug("Stock check response: {}", response);
        return ResponseEntity.ok(response);
    }
}
