package io.teamchallenge.dto.product;

import io.teamchallenge.dto.CategoryResponseDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

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
    private List<String> images;
    private String brand;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private LocalDateTime createdAt;
}
