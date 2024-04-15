package io.teamchallenge.mapper;

import io.teamchallenge.dto.CategoryResponseDto;
import io.teamchallenge.dto.ProductResponseDto;
import io.teamchallenge.entity.Product;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class ProductResponseDtoMapper extends AbstractConverter<Product,ProductResponseDto> {

    /**
     * Converts a Product entity to a corresponding ProductResponseDto object.
     *
     * @param product The Product entity to be converted.
     * @return ProductResponseDto representing the converted product with relevant information.
     */
    @Override
    protected ProductResponseDto convert(Product product) {
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
