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
    private List<String> images;
    private String name;
    private BigDecimal price;
}
