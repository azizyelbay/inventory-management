package com.oneamz.inventorymanagement.mapper;

import com.oneamz.inventorymanagement.dto.ProductDto;
import com.oneamz.inventorymanagement.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    public abstract ProductDto toDto(Product product);

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "categoryName", target = "category.name")
    public abstract Product toEntity(ProductDto dto);

    public abstract List<ProductDto> toDtoList(List<Product> productList);
}
