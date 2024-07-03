package io.teamchallenge.controller;

import io.teamchallenge.dto.attributes.AttributeAttributeValueDto;
import io.teamchallenge.dto.category.CategoryRequestDto;
import io.teamchallenge.dto.category.CategoryResponseDto;
import io.teamchallenge.service.impl.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for categories.
 * @author Niktia Malov
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Retrieves attribute and attribute values for a specific category in all products.
     *
     * @param categoryId The ID of the category for which to retrieve attribute and attribute values.
     * @return ResponseEntity containing a list of AttributeAttributeValueDto objects.
     */
    @GetMapping("/{categoryId}/attribute-attributeValues-in-products")
    public ResponseEntity<List<AttributeAttributeValueDto>> getAttributeAttributeValueInProducts(
        @PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getAttributeAttributeValueByCategoryInProducts(categoryId));
    }

    /**
     * Retrieves attribute and attribute values for a specific category.
     *
     * @param categoryId The ID of the category for which to retrieve attribute and attribute values.
     * @return ResponseEntity containing a list of AttributeAttributeValueDto objects.
     */
    @GetMapping("/{categoryId}/attribute-attributeValues")
    public ResponseEntity<List<AttributeAttributeValueDto>> getAttributeAttributeValue(
        @PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getAttributeAttributeValueByCategory(categoryId));
    }

    /**
     * Retrieves all categories.
     *
     * @return ResponseEntity containing a list of CategoryResponseDto objects.
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    /**
     * Creates a new category.
     *
     * @param categoryRequestDto the DTO containing the details of the category to create
     * @return a {@link ResponseEntity} containing the created category and HTTP status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@RequestBody CategoryRequestDto categoryRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(categoryService.create(categoryRequestDto));
    }
}
