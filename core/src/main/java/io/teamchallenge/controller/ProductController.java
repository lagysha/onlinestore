package io.teamchallenge.controller;

import io.teamchallenge.annotation.AllowedSortFields;
import io.teamchallenge.dto.filter.ProductFilterDto;
import io.teamchallenge.dto.pageable.AdvancedPageableDto;
import io.teamchallenge.dto.product.ProductRequestDto;
import io.teamchallenge.dto.product.ProductResponseDto;
import io.teamchallenge.dto.product.ShortProductResponseDto;
import io.teamchallenge.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Controller for products.
 * @author Niktia Malov
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;

    /**
     * Handles GET requests for retrieving a paginated list of products with optional filtering and sorting.
     *
     * @param productFilterDto DTO containing optional product filter criteria.
     * @param pageable Pageable object containing pagination and sorting information.
     *                 - The default sort field is "price".
     *                 - The default sort direction is descending.
     *                 - Only sorting by "price" is allowed.
     * @return ResponseEntity containing an advanced pageable DTO of ShortProductResponseDto objects.
     */
    @GetMapping
    public ResponseEntity<AdvancedPageableDto<ShortProductResponseDto>> getAll(
        @Valid ProductFilterDto productFilterDto,
        @AllowedSortFields(values = {"price"})
        @PageableDefault(sort = "price", direction = DESC) Pageable pageable) {
        return ResponseEntity.ok(productService.getAll(pageable, productFilterDto));
    }


    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id The identifier of the product to retrieve.
     * @return ResponseEntity containing the ProductResponseDto representing the retrieved product, with status OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    /**
     * Creates a new product based on the provided ProductRequestDto.
     *
     * @param productRequestDto The ProductRequestDto containing the details of the product to create.
     * @return ResponseEntity containing the ProductResponseDto representing the created product, with status CREATED.
     */
    @PostMapping
    public ResponseEntity<ProductResponseDto> create(
        @RequestBody @Valid ProductRequestDto productRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(productService.create(productRequestDto));
    }

    /**
     * Updates an existing product with the provided ID using the details from the ProductRequestDto.
     *
     * @param id                The identifier of the product to update.
     * @param productRequestDto The ProductRequestDto containing the updated details of the product.
     * @return ResponseEntity containing the ProductResponseDto representing the updated product, with status OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id,
                                                     @RequestBody @Valid ProductRequestDto productRequestDto) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(productService.update(id, productRequestDto));
    }

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id The identifier of the product to delete.
     * @return ResponseEntity with status NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}