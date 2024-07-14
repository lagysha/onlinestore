package io.teamchallenge.dto.cart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@Builder
@ToString
@EqualsAndHashCode
public class CartItemPatchRequestDto {
    @Min(value = 1,message = "The quantity of one product must be greater than 0")
    @Max(value = 999999, message = "The quantity of one product must be fewer than 1,000,000")
    private Integer quantity;
}
