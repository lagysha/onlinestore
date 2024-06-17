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
    //TODO: refactor to one link with main imagine
    private List<String> images;
    private String name;
    private BigDecimal price;
}
