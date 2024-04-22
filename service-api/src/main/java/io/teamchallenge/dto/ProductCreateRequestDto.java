package io.teamchallenge.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
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
public class ProductCreateRequestDto {
    @Size(min = 1,max = 255, message = "shortDesc is too long max size is 255 chars")
    private String shortDesc;
    private Long categoryId;
    private List<ProductAttributeRequestDto> productAttributeRequestDtos;
    private List<String> imageLinks;
    private Long brandId;
    @Size(min = 1,max = 255, message = "shortDesc is too long max size is 255 chars")
    private String name;
    private String desc;
    @Digits(integer = 10, fraction = 2,message = "Price must have an integer part less than 10 " +
        "and a fraction part less than 2")
    private BigDecimal price;
    private Integer quantity;
}
