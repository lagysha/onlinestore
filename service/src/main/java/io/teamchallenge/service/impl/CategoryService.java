package io.teamchallenge.service.impl;

import io.teamchallenge.constant.ExceptionMessage;
import io.teamchallenge.dto.attributes.AttributeAttributeValueDto;
import io.teamchallenge.dto.attributes.AttributeDto;
import io.teamchallenge.dto.attributes.AttributeValueResponseDto;
import io.teamchallenge.dto.category.CategoryAttributeAttributeValueVO;
import io.teamchallenge.dto.category.CategoryRequestDto;
import io.teamchallenge.dto.category.CategoryResponseDto;
import io.teamchallenge.entity.Category;
import io.teamchallenge.exception.DeletionException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.CategoryAttributeRepository;
import io.teamchallenge.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.teamchallenge.constant.ExceptionMessage.CATEGORY_DELETION_EXCEPTION_MESSAGE;

/**
 * Service class for managing categories.
 *
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

    /**
     * Creates a new category.
     *
     * <p>This method is transactional, meaning it will be executed within a database transaction.
     * If any part of the method fails, the transaction will be rolled back.</p>
     *
     * @param categoryRequestDto the data transfer object containing the details of the category to create.
     * @return CategoryResponseDto the data transfer object containing the details of the created category.
     */
    @Transactional
    public CategoryResponseDto create(CategoryRequestDto categoryRequestDto) {
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

    /**
     * Deletes a category by its ID.
     *
     * <p>This method retrieves a category by its ID and deletes it from the repository. If the category
     * is not found, a {@link NotFoundException} is thrown. If there is a data integrity violation during
     * the deletion process, a {@link DeletionException} is thrown.
     *
     * @param id the ID of the category to delete
     * @throws NotFoundException if the category with the specified ID is not found
     * @throws DeletionException if a data integrity violation occurs during deletion
     */
    @Transactional
    public void deleteById(Long id) {
        var retrievedCategory = categoryRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.CATEGORY_NOT_FOUND_BY_ID.formatted(id)));

        try {
            categoryRepository.deleteById(retrievedCategory.getId());
        } catch (DataIntegrityViolationException e) {
            throw new DeletionException(CATEGORY_DELETION_EXCEPTION_MESSAGE, e);
        }
    }

    /**
     * Updates a category by its ID with the provided update details.
     *
     * <p>This method retrieves a category by its ID and updates its name
     *    and description with the values provided in the
     * {@code categoryRequestDto}. If the category is not found, a {@link NotFoundException} is thrown.
     *
     * @param id the ID of the category to update
     * @param categoryRequestDto the DTO containing the update details
     * @return a {@link CategoryResponseDto} containing the updated category details
     * @throws NotFoundException if the category with the specified ID is not found
     */
    @Transactional
    public CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto) {
        var retrievedCategory = categoryRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.CATEGORY_NOT_FOUND_BY_ID.formatted(id)));

        retrievedCategory.setName(categoryRequestDto.getName());
        retrievedCategory.setDescription(categoryRequestDto.getDescription());

        return modelMapper.map(retrievedCategory,CategoryResponseDto.class);
    }

    private List<AttributeAttributeValueDto> collectToAttributeAttributeValueDtoList(
        Stream<CategoryAttributeAttributeValueVO> categoryAttributeAttributeValueVO) {
        return categoryAttributeAttributeValueVO
            .collect(Collectors.groupingBy(
                vo -> new AttributeDto(vo.getAttributeId(), vo.getAttributeName()),
                Collectors.mapping(
                    vo -> new AttributeValueResponseDto(vo.getAttributeValueId(), vo.getAttributeValueName()),
                    Collectors.toList()
                )
            ))
            .entrySet()
            .stream()
            .map(entry -> AttributeAttributeValueDto.builder()
                .id(entry.getKey().getId())
                .name(entry.getKey().getName())
                .attributeValueResponseDtos(entry.getValue())
                .build())
            .collect(Collectors.toList());
    }
}
