package io.teamchallenge.mapper;

import io.teamchallenge.dto.ImageDto;
import io.teamchallenge.dto.category.CategoryResponseDto;
import io.teamchallenge.dto.product.ProductAttributeResponseDto;
import io.teamchallenge.dto.product.ProductResponseDto;
import io.teamchallenge.entity.Image;
import io.teamchallenge.entity.reviews.Review;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.teamchallenge.util.Utils.getProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProductResponseDtoMapperTest {
    @InjectMocks
    private ProductResponseDtoMapper productResponseDtoMapper;

    @Test
    void convertTest() {
        var product = getProduct();

        var expected = ProductResponseDto.builder()
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
                .map((pa) -> new ProductAttributeResponseDto(
                    pa.getAttributeValue().getAttribute().getName(),
                    pa.getAttributeValue().getValue()))
                .collect(Collectors.toList()))
            .images(product.getImages()
                .stream()
                .map(img -> ImageDto.builder()
                    .link(img.getLink())
                    .order(img.getOrder())
                    .build())
                .collect(Collectors.toList()))
            .brand(product.getBrand().getName())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .quantity(product.getQuantity())
            .createdAt(product.getCreatedAt())
            .rating(product.getReviews().stream()
                .mapToInt(Review::getRate)
                .average()
                .orElse(0))
            .build();

        assertEquals(expected, productResponseDtoMapper.convert(product));
    }
}
