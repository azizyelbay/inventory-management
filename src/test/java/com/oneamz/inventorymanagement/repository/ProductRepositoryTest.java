package com.oneamz.inventorymanagement.repository;

import com.oneamz.inventorymanagement.model.Category;
import com.oneamz.inventorymanagement.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product product1;
    private Product product2;

    @BeforeEach
    public void setup() {
        Category electronics = new Category();
        electronics.setName("Electronics");
        categoryRepository.save(electronics);

        product1 = new Product();
        product1.setName("Laptop");
        product1.setPrice(new BigDecimal("1200.00"));
        product1.setQuantity(10);
        product1.setCategory(electronics);
        productRepository.save(product1);

        product2 = new Product();
        product2.setName("Smartphone");
        product2.setPrice(new BigDecimal("800.00"));
        product2.setQuantity(15);
        product2.setCategory(electronics);
        productRepository.save(product2);
    }

    @Test
    public void whenFindByIdIn_thenReturnProducts() {
        List<Long> productIds = Arrays.asList(product1.getId(), product2.getId());

        List<Product> foundProducts = productRepository.findByIdIn(productIds);

        assertThat(foundProducts).hasSize(2);
        assertThat(foundProducts).containsExactlyInAnyOrder(product1, product2);
    }

    @Test
    public void whenFindByIdIn_withNonExistingIds_thenReturnEmptyList() {
        List<Long> productIds = Arrays.asList(-1L, -2L);

        List<Product> foundProducts = productRepository.findByIdIn(productIds);

        assertThat(foundProducts).isEmpty();
    }
}
