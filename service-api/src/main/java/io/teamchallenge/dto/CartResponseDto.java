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
public class CartResponseDto {
    private List<CartItemResponseDto> cartItemResponseDtos;
    private BigDecimal totalPrice;
}
