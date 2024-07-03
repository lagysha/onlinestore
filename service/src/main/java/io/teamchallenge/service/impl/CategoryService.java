package io.teamchallenge.service.impl;

import io.teamchallenge.dto.attributes.AttributeAttributeValueDto;
import io.teamchallenge.dto.attributes.AttributeDto;
import io.teamchallenge.dto.attributes.AttributeValueDto;
import io.teamchallenge.dto.category.CategoryAttributeAttributeValueVO;
import io.teamchallenge.dto.category.CategoryRequestDto;
import io.teamchallenge.dto.category.CategoryResponseDto;
import io.teamchallenge.entity.Category;
import io.teamchallenge.repository.CategoryAttributeRepository;
import io.teamchallenge.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing categories.
 * @author Niktia Malov
 */
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryAttributeRepository categoryAttributeRepository;
    private final ModelMapper modelMapper;

    /**
     * Retrieves attribute and attribute values for a specific category in all products.
     *
     * @param categoryId The ID of the category for which to retrieve attribute and attribute values.
     * @return List of AttributeAttributeValueDto objects representing attribute and attribute values
     *         for the specified category.
     */
    public List<AttributeAttributeValueDto> getAttributeAttributeValueByCategoryInProducts(Long categoryId) {
        return collectToAttributeAttributeValueDtoList(categoryRepository
            .findAllAttributeAttributeValueByCategoryInProducts(categoryId));
    }

    /**
     * Retrieves attribute and attribute values for a specific category.
     *
     * @param categoryId The ID of the category for which to retrieve attribute and attribute values.
     * @return List of AttributeAttributeValueDto objects representing attribute and attribute values
     *         for the specified category.
     */
    public List<AttributeAttributeValueDto> getAttributeAttributeValueByCategory(Long categoryId) {
        return collectToAttributeAttributeValueDtoList(categoryAttributeRepository
            .findAllAttributeAttributeValueByCategory(categoryId));
    }

    /**
     * Retrieves all categories.
     *
     * @return List of CategoryResponseDto objects representing all categories.
     */
    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll().stream()
            .map(category -> modelMapper.map(category, CategoryResponseDto.class))
            .collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponseDto create(CategoryRequestDto categoryRequestDto){
        var savedCategory = categoryRepository.save(Category.builder()
                .name(categoryRequestDto.getName())
                .description(categoryRequestDto.getDescription())
            .build());

        return CategoryResponseDto.builder()
            .id(savedCategory.getId())
            .name(savedCategory.getName())
            .description(savedCategory.getDescription())
            .build();
    }

    private List<AttributeAttributeValueDto> collectToAttributeAttributeValueDtoList(
        Stream<CategoryAttributeAttributeValueVO> categoryAttributeAttributeValueVO) {
        return categoryAttributeAttributeValueVO
            .collect(Collectors.groupingBy(
                vo -> new AttributeDto(vo.getAttributeId(), vo.getAttributeName()),
                Collectors.mapping(
                    vo -> new AttributeValueDto(vo.getAttributeValueId(), vo.getAttributeValueName()),
                    Collectors.toList()
                )
            ))
            .entrySet()
            .stream()
            .map(entry -> AttributeAttributeValueDto.builder()
                .id(entry.getKey().getId())
                .name(entry.getKey().getName())
                .attributeValueDtos(entry.getValue())
                .build())
            .collect(Collectors.toList());
    }
}
