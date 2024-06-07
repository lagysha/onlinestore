package io.teamchallenge.mapper;

import io.teamchallenge.dto.category.CategoryResponseDto;
import io.teamchallenge.dto.product.ProductAttributeResponseDto;
import io.teamchallenge.dto.product.ProductResponseDto;
import io.teamchallenge.entity.Image;
import io.teamchallenge.entity.Product;
import java.util.stream.Collectors;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

/**
 * Mapper for {@link Product}.
 *
 * @author Niktia Malov
 */
@Component
public class ProductResponseDtoMapper extends AbstractConverter<Product, ProductResponseDto> {
    /**
     * Converts a Product entity to a corresponding ProductResponseDto object.
     *
     * @param product The Product entity to be converted.
     * @return ProductResponseDto representing the converted product with relevant information.
     */
    @Override
    protected ProductResponseDto convert(Product product) {
        return ProductResponseDto.builder()
            .id(product.getId())
            .shortDesc(product.getShortDesc())
            .categoryResponseDto(
                CategoryResponseDto.builder()
                    .id(product.getCategory().getId())
                    .description(product.getCategory().getDescription())
                    .name(product.getCategory().getName())
                    .build())
            .productAttributeResponseDtos(product.getProductAttributes()
                .stream()
                .map((pa) ->
                    ProductAttributeResponseDto
                        .builder()
                        .name(pa.getAttributeValue().getAttribute().getName())
                        .value(pa.getAttributeValue().getValue())
                        .build())
                .collect(Collectors.toList()))
            .images(product.getImages()
                .stream()
                .map(Image::getLink)
                .collect(Collectors.toList()))
            .brand(product.getBrand().getName())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .quantity(product.getQuantity())
            .createdAt(product.getCreatedAt())
            .build();
    }
}
