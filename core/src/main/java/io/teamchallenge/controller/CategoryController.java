package io.teamchallenge.controller;

import io.teamchallenge.dto.attributes.AttributeAttributeValueDto;
import io.teamchallenge.dto.category.CategoryRequestDto;
import io.teamchallenge.dto.category.CategoryResponseDto;
import io.teamchallenge.service.impl.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for categories.
 *
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
     * @param id The ID of the category for which to retrieve attribute and attribute values.
     * @return ResponseEntity containing a list of AttributeAttributeValueDto objects.
     */
    @GetMapping("/{id}/attribute-attributeValues-in-products")
    public ResponseEntity<List<AttributeAttributeValueDto>> getAttributeAttributeValueInProducts(
        @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getAttributeAttributeValueByCategoryInProducts(id));
    }

    /**
     * Retrieves attribute and attribute values for a specific category.
     *
     * @param id The ID of the category for which to retrieve attribute and attribute values.
     * @return ResponseEntity containing a list of AttributeAttributeValueDto objects.
     */
    @GetMapping("/{id}/attribute-attributeValues")
    public ResponseEntity<List<AttributeAttributeValueDto>> getAttributeAttributeValue(
        @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getAttributeAttributeValueByCategory(id));
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
    public ResponseEntity<CategoryResponseDto> create(@RequestBody CategoryRequestDto categoryRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(categoryService.create(categoryRequestDto));
    }

    /**
     * Deletes a category by its ID.
     *
     * <p>This method calls the {@code categoryService} to delete a category by its ID. If the deletion is successful,
     * it returns a response with HTTP status 204 (No Content).
     *
     * @param id the ID of the category to delete
     * @return a {@link ResponseEntity} with HTTP status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Updates a category by its ID with the provided update details.
     *
     * <p>This method calls the {@code categoryService} to update a category's details using a PUT request.
     * It returns the updated category details wrapped in a {@link ResponseEntity} with HTTP status 200 (OK).
     *
     * @param id the ID of the category to update
     * @param categoryRequestDto the DTO containing the update details
     * @return a {@link ResponseEntity} containing the updated category details with HTTP status 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable("id") Long id,
                                                      @RequestBody CategoryRequestDto categoryRequestDto) {
        return ResponseEntity.ok(categoryService.update(id, categoryRequestDto));
    }
}
