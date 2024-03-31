package com.oneamz.inventorymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CheckStockResponse {
    private String name;
    private Integer stockQuantity;
    private Boolean inStock;
}
