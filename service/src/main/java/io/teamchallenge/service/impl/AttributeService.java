package io.teamchallenge.service.impl;

import io.teamchallenge.dto.attributes.AttributeRequestDto;
import io.teamchallenge.dto.attributes.AttributeResponseDto;
import io.teamchallenge.entity.attributes.Attribute;
import io.teamchallenge.entity.attributes.CategoryAttribute;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.exception.PersistenceException;
import io.teamchallenge.repository.AttributeRepository;
import io.teamchallenge.repository.CategoryAttributeRepository;
import io.teamchallenge.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.teamchallenge.constant.ExceptionMessage.ATTRIBUTE_PERSISTENCE_EXCEPTION;
import static io.teamchallenge.constant.ExceptionMessage.CATEGORY_NOT_FOUND_BY_ID;

/**
 * Service class for managing attributes.
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
     * @throws NotFoundException if the category is not found by the given ID
     * @throws PersistenceException if there is a data integrity violation during the save operation
     */
    @Transactional
    public AttributeResponseDto create(AttributeRequestDto attributeRequestDto) {
        var attribute = attributeRepository.findByName(attributeRequestDto.getName())
            .orElseGet(() -> attributeRepository.save(Attribute.builder()
                .name(attributeRequestDto.getName())
                .build()));

        var category = categoryRepository.findById(attributeRequestDto.getCategoryId())
            .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND_BY_ID.formatted(attributeRequestDto.getCategoryId())));

        try {
            categoryAttributeRepository.save(CategoryAttribute.builder()
                .attribute(attribute)
                .category(category)
                .build());

            return AttributeResponseDto.builder()
                .id(attribute.getId())
                .name(attribute.getName())
                .build();
        }catch (DataIntegrityViolationException e) {
            throw new PersistenceException(ATTRIBUTE_PERSISTENCE_EXCEPTION, e);
        }

    }
}
