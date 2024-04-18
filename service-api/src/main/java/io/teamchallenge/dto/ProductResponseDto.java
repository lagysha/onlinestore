package io.teamchallenge.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class ProductResponseDto {
    private String shortDesc;
    private CategoryResponseDto categoryResponseDto;
    private List<ProductAttributeResponseDto> productAttributeResponseDtos;
    private String brand;
    private String name;
    private String desc;
    private BigDecimal price;
    private Integer quantity;
    private LocalDateTime createdAt;
}
