package io.teamchallenge.dto.pageable;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AdvancedPageableDto<T> extends PageableDto<T> {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
