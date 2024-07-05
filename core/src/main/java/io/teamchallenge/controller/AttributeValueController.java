package io.teamchallenge.controller;

import io.teamchallenge.dto.attributes.AttributeValuePatchRequestDto;
import io.teamchallenge.dto.attributes.AttributeValueResponseDto;
import io.teamchallenge.service.impl.AttributeValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for attributesValues.
 *
 * @author Niktia Malov
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attributeValues")
public class AttributeValueController {
    private final AttributeValueService attributeValueService;

    /**
     * Deletes an attribute value by its ID.
     *
     * <p>This method calls the {@code attributeValuesService} to delete an attribute value by its ID.
     * If the deletion is successful,
     * it returns a response with HTTP status 204 (No Content).
     *
     * @param id the ID of the attribute value to delete
     * @return a {@link ResponseEntity} with HTTP status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        attributeValueService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Partially updates an attribute value by its ID with the provided update details.
     *
     * <p>This method calls the {@code attributeValuesService}
     * to update an attribute value's details using a patch request.
     * It returns the updated attribute value details wrapped in a {@link ResponseEntity} with HTTP status 200 (OK).
     *
     * @param id                            the ID of the attribute value to update
     * @param attributeValuePatchRequestDto the DTO containing the update details
     * @return a {@link ResponseEntity} containing the updated attribute value details with HTTP status 200 (OK)
     */
    @PatchMapping("/{id}")
    public ResponseEntity<AttributeValueResponseDto> patchUpdate(@PathVariable("id") Long id,
                                                                 @RequestBody
                                                                 AttributeValuePatchRequestDto
                                                                     attributeValuePatchRequestDto) {
        return ResponseEntity.ok(attributeValueService.update(id, attributeValuePatchRequestDto));
    }
}