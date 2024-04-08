package io.teamchallenge.mapper;

import io.teamchallenge.dto.CategoryResponseDto;
import io.teamchallenge.dto.ProductResponseDto;
import io.teamchallenge.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductResponseDtoMapper {

    public ProductResponseDto convertToDto(Product product){
        return ProductResponseDto.builder()
            .shortDesc(product.getShortDesc())
            .categoryResponseDto(
                CategoryResponseDto.builder()
                .desc(product.getCategory().getDesc())
                .name(product.getCategory().getName())
                .build())
            .characteristics(product.getCharacteristics())
            .name(product.getName())
            .desc(product.getDesc())
            .price(product.getPrice())
            .quantity(product.getQuantity())
            .createdAt(product.getCreatedAt())
            .build();
    }
}
