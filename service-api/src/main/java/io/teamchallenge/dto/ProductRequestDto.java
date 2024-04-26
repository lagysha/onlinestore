package io.teamchallenge.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
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
public class ProductRequestDto {
    @Size(min = 1,max = 255, message = "shortDesc is too long. Max size is 255 chars")
    private String shortDesc;
    private Long categoryId;
    private List<Long> attributeValueId;
    @Size(min = 1, max = 10, message = "Pass between 1 and 10 images")
    private List<String> imageLinks;
    private Long brandId;
    @Size(min = 1,max = 255, message = "name is too long. Max size is 255 chars")
    private String name;
    @NotBlank
    private String description;
    @Digits(integer = 10, fraction = 2,message = "Price must have an integer part less than 10 " +
        "and a fraction part less than 2")
    private BigDecimal price;
    private Integer quantity;
}
