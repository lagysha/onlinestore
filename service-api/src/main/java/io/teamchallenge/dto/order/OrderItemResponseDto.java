package io.teamchallenge.dto.order;

import io.teamchallenge.dto.product.ShortProductResponseDto;
import java.math.BigDecimal;
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
public class OrderItemResponseDto {
    private ShortProductResponseDto shortProductResponseDto;
    private Integer quantity;
    private BigDecimal price;
}
