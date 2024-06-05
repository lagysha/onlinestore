package io.teamchallenge.controller;

import io.teamchallenge.dto.attributes.AttributeAttributeValueDto;
import io.teamchallenge.dto.category.CategoryResponseDto;
import io.teamchallenge.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Niktia Malov
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

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
    public ResponseEntity<List<CategoryResponseDto>> getAll(){
        return ResponseEntity.ok(categoryService.getAll());
    }
}
