package com.oneamz.inventorymanagement.service;

import com.oneamz.inventorymanagement.dto.CheckStockResponse;
import com.oneamz.inventorymanagement.dto.ProductDto;
import com.oneamz.inventorymanagement.exception.ProductNotFoundException;
import com.oneamz.inventorymanagement.mapper.ProductMapper;
import com.oneamz.inventorymanagement.model.Category;
import com.oneamz.inventorymanagement.model.Product;
import com.oneamz.inventorymanagement.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;

    @Transactional
    public ProductDto saveProduct(ProductDto productDTO) {
        log.debug("Saving product: {}", productDTO.getName());

        final Category category = categoryService.findCategoryByName(productDTO.getCategoryName());
        final Product product = productMapper.toEntity(productDTO);
        product.setCategory(category);
        final Product savedProduct = productRepository.save(product);

        log.debug("Product saved: {}", savedProduct.getName());
        return productMapper.toDto(savedProduct);
    }

    public List<ProductDto> getAllProducts() {
        log.debug("Fetching all products");
        return productMapper.toDtoList(productRepository.findAll());
    }

    public ProductDto getProductById(Long productId) {
        log.debug("Fetching product by id: {}", productId);
        return productMapper.toDto(findProductById(productId));
    }

    protected Product findProductById(Long id) {
        log.debug("Finding product by id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(
                        () -> {
                            log.error("Product not found by id: {}", id);
                            return new ProductNotFoundException("Product could not find by id: " + id);
                        });
    }

    @Transactional
    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        log.debug("Updating product with id: {}", productId);
        final Product product = findProductById(productId);

        product.setName(productDto.getName());
        product.setQuantity(productDto.getQuantity());
        product.setPrice(productDto.getPrice());

        final Category category = categoryService.findCategoryByName(productDto.getCategoryName());
        product.setCategory(category);

        return productMapper.toDto(productRepository.save(product));
    }

    public void deleteProduct(Long productId) {
        log.debug("Deleting product with id: {}", productId);
        findProductById(productId);
        productRepository.deleteById(productId);
        log.debug("Product deleted with id: {}", productId);
    }

    @Transactional
    public List<CheckStockResponse> checkStock(List<Long> productId) {
        log.debug("Checking stock for products: {}", productId);
        return productRepository.findByIdIn(productId)
                .stream().map(product -> CheckStockResponse.builder()
                        .name(product.getName())
                        .stockQuantity(product.getQuantity())
                        .inStock(product.getQuantity() > 0)
                        .build()).collect(Collectors.toList());
    }
}
