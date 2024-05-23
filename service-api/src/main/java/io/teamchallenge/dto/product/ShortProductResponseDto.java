package io.teamchallenge.dto.product;

import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class ShortProductResponseDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private List<String> images;
}
