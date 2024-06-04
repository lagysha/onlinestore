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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{categoryId}/attribute-attributeValues")
    public ResponseEntity<List<AttributeAttributeValueDto>> getAttributeAttributeValue(
        @PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getAttributeAttributeValueByCategory(categoryId));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAll(){
        return ResponseEntity.ok(categoryService.getAll());
    }
}
