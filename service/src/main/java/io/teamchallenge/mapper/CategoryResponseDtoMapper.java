package io.teamchallenge.mapper;

import io.teamchallenge.dto.category.CategoryResponseDto;
import io.teamchallenge.entity.Category;
import org.modelmapper.AbstractConverter;

/**
 * Mapper for {@link Category}.
 * @author Niktia Malov
 */
public class CategoryResponseDtoMapper extends AbstractConverter<Category, CategoryResponseDto> {
    /**
     * Converts a Category entity into a CategoryResponseDto object.
     *
     * @param category The Category entity to convert.
     * @return The converted CategoryResponseDto object.
     */
    @Override
    protected CategoryResponseDto convert(Category category) {
        return CategoryResponseDto.builder()
            .id(category.getId())
            .name(category.getName())
            .description(category.getDescription())
            .build();
    }
}
