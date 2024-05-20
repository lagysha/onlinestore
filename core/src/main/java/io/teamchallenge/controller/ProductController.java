package io.teamchallenge.controller;

import io.teamchallenge.annotation.AllowedSortFields;
import io.teamchallenge.dto.PageableDto;
import io.teamchallenge.dto.ProductRequestDto;
import io.teamchallenge.dto.ProductResponseDto;
import io.teamchallenge.dto.ShortProductResponseDto;
import io.teamchallenge.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import static org.springframework.data.domain.Sort.Direction.DESC;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;

    /**
     * Retrieves a pageable list of products based on optional filtering by name and pageable parameters.
     *
     * @param name     Optional parameter for filtering products by name.
     * @param pageable Pageable object specifying pagination and sorting parameters.
     *                 Defaults to sorting by creation date in descending order if not specified.
     * @return ResponseEntity containing a PageableDto of ProductResponseDto,
     *         representing the paginated list of products.
     */
    @GetMapping
    public ResponseEntity<PageableDto<ShortProductResponseDto>> getAll(@RequestParam(required = false) String name,
                                                                       @AllowedSortFields(values = {"name", "quantity",
                                                                           "price", "createdAt"})
                                                                       @PageableDefault(sort = "createdAt",
                                                                           direction = DESC)
                                                                       Pageable pageable) {
        return ResponseEntity.ok(productService.getAll(pageable, name));
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