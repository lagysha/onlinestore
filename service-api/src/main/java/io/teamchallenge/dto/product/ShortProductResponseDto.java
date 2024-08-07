package io.teamchallenge.dto.product;

import io.teamchallenge.dto.ImageDto;
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
@ToString
@Builder
@EqualsAndHashCode
public class ShortProductResponseDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private List<ImageDto> images;
    private Boolean available;
    private Double rating;
}
