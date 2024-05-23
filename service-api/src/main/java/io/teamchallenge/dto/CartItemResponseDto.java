package io.teamchallenge.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class CartItemResponseDto {
    private Long productId;
    private Integer quantity;
    //TODO : decide how to identify the main image. Add additional field to image table with its order
    //TODO : and here retrieve image with Additional number 1 like its order
    private List<String> images;
    private String name;
    private BigDecimal price;
}
