package com.oneamz.inventorymanagement.service;

import com.oneamz.inventorymanagement.model.Category;
import com.oneamz.inventorymanagement.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;

    private Category existingCategory;
    private final String categoryName = "Electronics";

    @BeforeEach
    void setUp() {
        existingCategory = Category.builder()
                .name(categoryName)
                .build();
    }

    @Test
    void testFindCategoryByNameWhenCategoryExistsThenReturnExistingCategory() {
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(existingCategory));

        Category result = categoryService.findCategoryByName(categoryName);

        assertThat(result).isSameAs(existingCategory);
        verify(categoryRepository, times(1)).findByName(categoryName);
    }

    @Test
    void testFindCategoryByNameWhenCategoryDoesNotExistThenCreateNewCategory() {
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Category result = categoryService.findCategoryByName(categoryName);

        assertThat(result.getName()).isEqualTo(categoryName);
        verify(categoryRepository, times(1)).findByName(categoryName);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testFindCategoryByNameWhenCategoryDoesNotExistAndExceptionOccursThenThrowException() {
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> categoryService.findCategoryByName(categoryName))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database error");

        verify(categoryRepository, times(1)).findByName(categoryName);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
}
