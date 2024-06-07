package io.teamchallenge.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddReviewRequestDto {
    @NotBlank
    @Size(max = 3000, message = "Message length must be no more than 3000 characters")
    private String text;

    @NotNull
    @Min(value = 1, message = "Rate can not be smaller than 1")
    @Max(value = 5, message = "Rate can not be bigger than 5")
    private Short rate;
}
