package io.teamchallenge.dto.filter;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceFilter {
    @Min(value = 0)
    private Integer from;
    @Min(value = 0)
    private Integer to;
}
