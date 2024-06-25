package io.teamchallenge.dto.product;

import io.teamchallenge.dto.ImageDto;
import io.teamchallenge.dto.category.CategoryResponseDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class ProductResponseDto {
    private Long id;
    private String shortDesc;
    private CategoryResponseDto categoryResponseDto;
    private List<ProductAttributeResponseDto> productAttributeResponseDtos;
    private List<ImageDto> images;
    private String brand;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private LocalDateTime createdAt;
}
