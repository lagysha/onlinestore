package io.teamchallenge.dto.product;

import io.teamchallenge.dto.attributes.AttributeAttributeValueRequestDto;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class ProductRequestDto {
    @NotBlank
    @Size(min = 1,max = 255, message = "shortDesc is too long. Max size is 255 chars")
    private String shortDesc;
    private Long categoryId;
    private List<Long> attributeValueIds;
    private List<AttributeAttributeValueRequestDto> attributeAttributeValueRequestDtos;
    private Long brandId;
    @Size(min = 1,max = 255, message = "name is too long. Max size is 255 chars")
    private String name;
    @NotBlank
    @Size(min = 1,max = 1000, message = "shortDesc is too long. Max size is 1000 chars")
    private String description;
    @Digits(integer = 10, fraction = 2,message = "Price must have an integer part less than 10 "
        + "and a fraction part less than 2")
    private BigDecimal price;
    @Min(value = 1,message = "The quantity of one product must be greater than 0")
    private Integer quantity;
}
