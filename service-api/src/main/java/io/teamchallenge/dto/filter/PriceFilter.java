package io.teamchallenge.dto.filter;

import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PriceFilter {
   @Min(value = 0)
   private Integer from;
   @Min(value = 0)
   private Integer to;
}
