package io.teamchallenge.mapper;

import io.teamchallenge.dto.category.CategoryResponseDto;
import io.teamchallenge.entity.Category;
import org.modelmapper.AbstractConverter;

public class CategoryResponseDtoMapper extends AbstractConverter<Category, CategoryResponseDto> {
    @Override
    protected CategoryResponseDto convert(Category category) {
        return CategoryResponseDto.builder()
            .id(category.getId())
            .name(category.getName())
            .description(category.getDescription())
            .build();
    }
}
