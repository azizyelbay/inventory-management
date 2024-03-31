package com.oneamz.inventorymanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneamz.inventorymanagement.dto.CheckStockResponse;
import com.oneamz.inventorymanagement.dto.ProductDto;
import com.oneamz.inventorymanagement.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    @Mock
    private ProductService productService;
    @InjectMocks
    private ProductController productController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetProducts() throws Exception {
        // Arrange
        List<ProductDto> products = Arrays.asList(
                new ProductDto(1L, "Product 1", 10, new BigDecimal("19.99"), 1L, "Category 1"),
                new ProductDto(2L, "Product 2", 20, new BigDecimal("29.99"), 2L, "Category 2")
        );
        when(productService.getAllProducts()).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/v1/product"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(products)));

        verify(productService).getAllProducts();
    }

    @Test
    public void testSaveProduct() throws Exception {
        // Arrange
        ProductDto request = new ProductDto(null, "New Product", 15, new BigDecimal("39.99"), 3L, "Category 3");
        ProductDto response = new ProductDto(3L, "New Product", 15, new BigDecimal("39.99"), 3L, "Category 3");
        when(productService.saveProduct(any(ProductDto.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(productService).saveProduct(any(ProductDto.class));
    }

    @Test
    public void testGetProductById() throws Exception {
        // Arrange
        Long productId = 1L;
        ProductDto product = new ProductDto(productId, "Product 1", 10, new BigDecimal("19.99"), 1L, "Category 1");
        when(productService.getProductById(productId)).thenReturn(product);

        // Act & Assert
        mockMvc.perform(get("/api/v1/product/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(product)));

        verify(productService).getProductById(productId);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        // Arrange
        Long productId = 1L;
        ProductDto request = new ProductDto(productId, "Updated Product", 20, new BigDecimal("24.99"), 1L, "Category 1");
        ProductDto response = new ProductDto(productId, "Updated Product", 20, new BigDecimal("24.99"), 1L, "Category 1");
        when(productService.updateProduct(eq(productId), any(ProductDto.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/api/v1/product/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(productService).updateProduct(eq(productId), any(ProductDto.class));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        // Arrange
        Long productId = 1L;
        doNothing().when(productService).deleteProduct(productId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/product/{productId}", productId))
                .andExpect(status().isOk());

        verify(productService).deleteProduct(productId);
    }

    @Test
    public void testCheckStock() throws Exception {
        // Arrange
        List<Long> productIds = Arrays.asList(1L, 2L);
        List<CheckStockResponse> stockResponses = Arrays.asList(
                CheckStockResponse.builder().name("Product 1").stockQuantity(10).inStock(true).build(),
                CheckStockResponse.builder().name("Product 2").stockQuantity(0).inStock(false).build()
        );
        when(productService.checkStock(productIds)).thenReturn(stockResponses);

        // Act & Assert
        mockMvc.perform(get("/api/v1/product/check-stock")
                        .param("productId", productIds.stream().map(Object::toString).toArray(String[]::new)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(stockResponses)));

        verify(productService).checkStock(productIds);
    }
}
