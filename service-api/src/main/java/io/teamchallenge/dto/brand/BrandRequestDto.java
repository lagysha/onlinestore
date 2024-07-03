package io.teamchallenge.dto.brand;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandRequestDto {
    private String name;
}
