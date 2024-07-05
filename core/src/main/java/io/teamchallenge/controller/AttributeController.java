package io.teamchallenge.controller;

import io.teamchallenge.dto.attributes.AttributeRequestDto;
import io.teamchallenge.dto.attributes.AttributeRequestUpdateDto;
import io.teamchallenge.dto.attributes.AttributeResponseDto;
import io.teamchallenge.service.impl.AttributeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for attributes.
 *
 * @author Niktia Malov
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attributes")
public class AttributeController {
    private final AttributeService attributeService;

    /**
     * Creates a new attribute.
     *
     * @param attributeRequestDto the DTO containing the details of the attribute to create
     * @return a {@link ResponseEntity} containing the created attribute and HTTP status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<AttributeResponseDto> create(
        @RequestBody @Valid AttributeRequestDto attributeRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(attributeService.create(attributeRequestDto));
    }

    /**
     * Deletes an attribute by its ID.
     *
     * <p>This method calls the {@code attributeService} to delete an attribute by its ID.
     * If the deletion is successful,
     * it returns a response with HTTP status 204 (No Content).
     *
     * @param id the ID of the attribute to delete
     * @return a {@link ResponseEntity} with HTTP status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        attributeService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Partially updates an attribute by its ID with the provided update details.
     *
     * <p>This method calls the {@code attributeService} to update an attribute's details using a patch request.
     * It returns the updated attribute details wrapped in a {@link ResponseEntity} with HTTP status 200 (OK).
     *
     * @param id                            the ID of the attribute to update
     * @param attributeValuePatchRequestDto the DTO containing the update details
     * @return a {@link ResponseEntity} containing the updated attribute details with HTTP status 200 (OK)
     */
    @PatchMapping("/{id}")
    public ResponseEntity<AttributeResponseDto> patchUpdate(@PathVariable("id") Long id,
                                                            @RequestBody
                                                            AttributeRequestUpdateDto attributeValuePatchRequestDto) {
        return ResponseEntity.ok(attributeService.update(id, attributeValuePatchRequestDto));
    }
}
