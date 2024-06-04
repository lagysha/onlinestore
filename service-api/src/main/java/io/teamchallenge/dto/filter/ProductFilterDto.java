package io.teamchallenge.dto.filter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProductFilterDto {
    @NotBlank
    private String name;
    private PriceFilter price;
    @Size(min = 1)
    private List<Long> brandIds;
    @Min(value = 1)
    private Long categoryId;
    @Size(min = 1)
    private List<Long> attributeValueIds;
}