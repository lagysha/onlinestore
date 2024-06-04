package io.teamchallenge.dto.product;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProductMinMaxPriceDto {
    private BigDecimal min;
    private BigDecimal max;
}
