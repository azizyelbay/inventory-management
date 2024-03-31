package com.oneamz.inventorymanagement.repository;

import com.oneamz.inventorymanagement.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setup() {
        // Pre-populate the database with a category, if necessary.
        Category electronics = new Category();
        electronics.setName("Electronics");
        categoryRepository.save(electronics);
    }

    @Test
    public void whenFindByName_thenReturnCategory() {
        // Given
        String name = "Electronics";

        // When
        Optional<Category> foundCategory = categoryRepository.findByName(name);

        // Then
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo(name);
    }

    @Test
    public void whenFindByName_withNonExistingName_thenReturnEmpty() {
        // Given
        String name = "NonExistingCategory";

        // When
        Optional<Category> foundCategory = categoryRepository.findByName(name);

        // Then
        assertThat(foundCategory).isNotPresent();
    }
}
