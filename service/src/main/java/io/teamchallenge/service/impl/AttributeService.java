package io.teamchallenge.service.impl;

import io.teamchallenge.constant.ExceptionMessage;
import io.teamchallenge.dto.attributes.AttributeRequestDto;
import io.teamchallenge.dto.attributes.AttributeRequestUpdateDto;
import io.teamchallenge.dto.attributes.AttributeResponseDto;
import io.teamchallenge.entity.attributes.Attribute;
import io.teamchallenge.entity.attributes.CategoryAttribute;
import io.teamchallenge.exception.DeletionException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.exception.PersistenceException;
import io.teamchallenge.repository.AttributeRepository;
import io.teamchallenge.repository.CategoryAttributeRepository;
import io.teamchallenge.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.teamchallenge.constant.ExceptionMessage.ATTRIBUTE_DELETION_EXCEPTION_MESSAGE;
import static io.teamchallenge.constant.ExceptionMessage.ATTRIBUTE_PERSISTENCE_EXCEPTION;
import static io.teamchallenge.constant.ExceptionMessage.CATEGORY_NOT_FOUND_BY_ID;

/**
 * Service class for managing attributes.
 *
 * @author Niktia Malov
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttributeService {
    private final CategoryAttributeRepository categoryAttributeRepository;
    private final AttributeRepository attributeRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Creates a new attribute and associates it with a category.
     *
     * @param attributeRequestDto the DTO containing the details of the attribute to create
     * @return an {@link AttributeResponseDto} containing the details of the created attribute
     * @throws NotFoundException    if the category is not found by the given ID
     * @throws PersistenceException if there is a data integrity violation during the save operation
     */
    @Transactional
    public AttributeResponseDto create(AttributeRequestDto attributeRequestDto) {
        var attribute = attributeRepository.findByName(attributeRequestDto.getName())
            .orElseGet(() -> attributeRepository.save(Attribute.builder()
                .name(attributeRequestDto.getName())
                .build()));

        var category = categoryRepository.findById(attributeRequestDto.getCategoryId())
            .orElseThrow(
                () -> new NotFoundException(CATEGORY_NOT_FOUND_BY_ID.formatted(attributeRequestDto.getCategoryId())));

        try {
            categoryAttributeRepository.save(CategoryAttribute.builder()
                .attribute(attribute)
                .category(category)
                .build());

            return AttributeResponseDto.builder()
                .id(attribute.getId())
                .name(attribute.getName())
                .build();
        } catch (DataIntegrityViolationException e) {
            throw new PersistenceException(ATTRIBUTE_PERSISTENCE_EXCEPTION, e);
        }
    }

    /**
     * Deletes an attribute by its ID.
     *
     * <p>This method retrieves an attribute by its ID and deletes it from the repository. If the attribute
     * is not found, a {@link NotFoundException} is thrown. If there is a data integrity violation during
     * the deletion process, a {@link DeletionException} is thrown.
     *
     * @param id the ID of the attribute to delete
     * @throws NotFoundException if the attribute with the specified ID is not found
     * @throws DeletionException if a data integrity violation occurs during deletion
     */
    @Transactional
    public void deleteById(Long id) {
        var retrievedAttributeValue = attributeRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.ATTRIBUTE_NOT_FOUND_BY_ID.formatted(id)));

        try {
            attributeRepository.deleteById(retrievedAttributeValue.getId());
        } catch (DataIntegrityViolationException e) {
            throw new DeletionException(ATTRIBUTE_DELETION_EXCEPTION_MESSAGE, e);
        }
    }

    /**
     * Updates an attribute by its ID with the provided update details.
     *
     * <p>This method retrieves an attribute by its ID and updates its name with the value provided in the
     * {@code attributeRequestUpdateDto}. If the attribute is not found, a {@link NotFoundException} is thrown.
     *
     * @param id the ID of the attribute to update
     * @param attributeRequestUpdateDto the DTO containing the update details
     * @return an {@link AttributeResponseDto} containing the updated attribute details
     * @throws NotFoundException if the attribute with the specified ID is not found
     */
    @Transactional
    public AttributeResponseDto update(Long id, AttributeRequestUpdateDto attributeRequestUpdateDto) {
        var retrievedAttribute = attributeRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.ATTRIBUTE_NOT_FOUND_BY_ID.formatted(id)));

        retrievedAttribute.setName(attributeRequestUpdateDto.getName());

        return AttributeResponseDto
            .builder()
            .id(retrievedAttribute.getId())
            .name(retrievedAttribute.getName())
            .build();
    }
}
