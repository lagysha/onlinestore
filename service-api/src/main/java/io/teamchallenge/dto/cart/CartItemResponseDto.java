package io.teamchallenge.dto.cart;

import java.math.BigDecimal;
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
