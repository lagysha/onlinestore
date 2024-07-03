package io.teamchallenge.controller;

import io.teamchallenge.dto.attributes.AttributeRequestDto;
import io.teamchallenge.dto.attributes.AttributeResponseDto;
import io.teamchallenge.service.impl.AttributeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for attributes.
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

    //TODO: endpoint to delete Attribute
}
