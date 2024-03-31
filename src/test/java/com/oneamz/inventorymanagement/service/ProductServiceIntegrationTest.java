package com.oneamz.inventorymanagement.service;

import com.oneamz.inventorymanagement.dto.ProductDto;
import com.oneamz.inventorymanagement.mapper.ProductMapper;
import com.oneamz.inventorymanagement.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext
@Transactional
@ActiveProfiles("test")
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Test
    void getAllProducts_ShouldReturnAllProductsFromDatabase() {
        // Arrange
        ProductDto product1 = new ProductDto();
        product1.setName("Test Product");
        product1.setPrice(new BigDecimal(50.0));
        product1.setCategoryName("test category");
        productService.saveProduct(product1);

        ProductDto product2 = new ProductDto();
        product2.setName("Test Product");
        product2.setPrice(new BigDecimal(50.0));
        product2.setCategoryName("test category");
        productService.saveProduct(product2);

        // Act
        List<ProductDto> result = productService.getAllProducts();

        // Assert
        assertEquals(2, result.size());
        // Add more assertions if needed
    }

    @Test
    void createProduct_ShouldSaveProductToDatabase() {
        // Arrange
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setPrice(new BigDecimal(50.0));
        productDto.setCategoryName("test category");

        // Act
        productService.saveProduct(productDto);

        // Assert
        assertEquals(1, productService.getAllProducts().size());
        // Add more assertions if needed
    }

}
