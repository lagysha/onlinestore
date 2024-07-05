package io.teamchallenge.dto.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandRequestDto {
    @NotBlank
    @Size(min = 1,max = 100, message = "name is too long. Max size is 100 chars")
    private String name;
}
