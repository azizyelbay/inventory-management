package com.oneamz.inventorymanagement.service;

import com.oneamz.inventorymanagement.model.Category;
import com.oneamz.inventorymanagement.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category findCategoryByName(String categoryName) {
        log.debug("Finding category by name: {}", categoryName);
        return categoryRepository.findByName(categoryName)
                .orElseGet(() -> {
                    log.debug("Category not found, creating new category with name: {}", categoryName);
                    Category category = Category.builder()
                            .name(categoryName)
                            .build();
                    category = categoryRepository.save(category);
                    log.debug("New category created: {}", category);
                    return category;
                });
    }
}
