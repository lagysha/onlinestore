package io.teamchallenge.service.impl;

import io.teamchallenge.constant.ExceptionMessage;
import io.teamchallenge.dto.attributes.AttributeValuePatchRequestDto;
import io.teamchallenge.dto.attributes.AttributeValueResponseDto;
import io.teamchallenge.exception.DeletionException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.AttributeValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.teamchallenge.constant.ExceptionMessage.ATTRIBUTEVALUE_DELETION_EXCEPTION_MESSAGE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttributeValueService {
    private final AttributeValueRepository attributeValueRepository;

    /**
     * Deletes an attribute value by its ID.
     *
     * <p>This method retrieves an attribute value by its ID and deletes it from the repository. If the attribute value
     * is not found, a {@link NotFoundException} is thrown. If there is a data integrity violation during
     * the deletion process, a {@link DeletionException} is thrown.
     *
     * @param id the ID of the attribute value to delete
     * @throws NotFoundException if the attribute value with the specified ID is not found
     * @throws DeletionException if a data integrity violation occurs during deletion
     */
    @Transactional
    public void deleteById(Long id) {
        var retrievedAttributeValue = attributeValueRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.ATTRIBUTEVALUE_NOT_FOUND_BY_ID.formatted(id)));

        try {
            attributeValueRepository.deleteById(retrievedAttributeValue.getId());
        } catch (DataIntegrityViolationException e) {
            throw new DeletionException(ATTRIBUTEVALUE_DELETION_EXCEPTION_MESSAGE, e);
        }
    }

    /**
     * Updates an attribute value by its ID with the provided update details.
     *
     * <p>This method retrieves an attribute value by its ID and updates its value with the value provided in the
     * {@code attributeValuePatchRequestDto}. If the attribute value is not found,
     *                                        a {@link NotFoundException} is thrown.
     *
     * @param id                            the ID of the attribute value to update
     * @param attributeValuePatchRequestDto the DTO containing the update details
     * @return an {@link AttributeValueResponseDto} containing the updated attribute value details
     * @throws NotFoundException if the attribute value with the specified ID is not found
     */
    @Transactional
    public AttributeValueResponseDto update(Long id, AttributeValuePatchRequestDto attributeValuePatchRequestDto) {
        var retrievedAttributeValue = attributeValueRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.ATTRIBUTEVALUE_NOT_FOUND_BY_ID.formatted(id)));

        retrievedAttributeValue.setValue(attributeValuePatchRequestDto.getValue());

        return AttributeValueResponseDto
            .builder()
            .id(retrievedAttributeValue.getId())
            .value(retrievedAttributeValue.getValue())
            .build();
    }
}
