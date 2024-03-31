package com.oneamz.inventorymanagement.service;

import com.oneamz.inventorymanagement.dto.CheckStockResponse;
import com.oneamz.inventorymanagement.dto.ProductDto;
import com.oneamz.inventorymanagement.exception.ProductNotFoundException;
import com.oneamz.inventorymanagement.mapper.ProductMapper;
import com.oneamz.inventorymanagement.model.Category;
import com.oneamz.inventorymanagement.model.Product;
import com.oneamz.inventorymanagement.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentMatchers;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    private ProductDto productDto;
    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setDescription( "Electronic Goods");
        category.setProducts(Collections.emptyList());
        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setQuantity(10);
        product.setPrice(new BigDecimal("999.99"));
        product.setCategory(category);
        productDto = new ProductDto(1L, "Laptop", 10, new BigDecimal("999.99"), 1L, "Electronics");
    }

    @Test
    void testSaveProductWhenValidProductThenProductSaved() {
        when(categoryService.findCategoryByName(productDto.getCategoryName())).thenReturn(category);
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto savedProductDto = productService.saveProduct(productDto);

        assertThat(savedProductDto).isNotNull();
        verify(productRepository).save(product);
    }

    @Test
    void testGetAllProductsThenAllProductsReturned() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));
        when(productMapper.toDtoList(Collections.singletonList(product))).thenReturn(Collections.singletonList(productDto));

        List<ProductDto> productDtos = productService.getAllProducts();

        assertThat(productDtos).isNotNull();
        assertThat(productDtos).hasSize(1);
    }

    @Test
    void testGetProductByIdWhenValidIdThenProductReturned() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto resultProductDto = productService.getProductById(1L);

        assertThat(resultProductDto).isNotNull();
        assertThat(resultProductDto.getId()).isEqualTo(productDto.getId());
    }

    @Test
    void testGetProductByIdWhenInvalidIdThenProductNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.getProductById(1L));
    }

    @Test
    void testUpdateProductWhenValidIdAndProductThenProductUpdated() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryService.findCategoryByName(productDto.getCategoryName())).thenReturn(category);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto updatedProductDto = productService.updateProduct(1L, productDto);

        assertThat(updatedProductDto).isNotNull();
        assertThat(updatedProductDto.getName()).isEqualTo(productDto.getName());
    }

    @Test
    void testUpdateProductWhenInvalidIdThenProductNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.updateProduct(1L, productDto));
    }

    @Test
    void testDeleteProductWhenValidIdThenProductDeleted() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository).findById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void testDeleteProductWhenInvalidIdThenProductNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.deleteProduct(1L));
    }

    @Test
    void testCheckStockWhenValidIdsThenStockChecked() {
        when(productRepository.findByIdIn(ArgumentMatchers.anyList())).thenReturn(Collections.singletonList(product));

        List<CheckStockResponse> stockResponses = productService.checkStock(Collections.singletonList(1L));

        assertThat(stockResponses).isNotNull();
        assertThat(stockResponses).hasSize(1);
        assertThat(stockResponses.get(0).getInStock()).isTrue();
    }

    @Test
    void testCheckStockWhenInvalidIdsThenStockChecked() {
        when(productRepository.findByIdIn(ArgumentMatchers.anyList())).thenReturn(Collections.emptyList());

        List<CheckStockResponse> stockResponses = productService.checkStock(Collections.singletonList(1L));

        assertThat(stockResponses).isEmpty();
    }
}
