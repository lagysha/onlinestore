package io.teamchallenge.dto.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryRequestDto {
    private String name;
    private String description;
}
