package io.teamchallenge.controllers;

import io.teamchallenge.annatations.AllowedSortFields;
import io.teamchallenge.dto.PageableDto;
import io.teamchallenge.dto.ProductResponseDto;
import io.teamchallenge.exception.ProductNotFoundException;
import io.teamchallenge.service.ProductService;
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
     * @param name      Optional parameter for filtering products by name.
     * @param pageable  Pageable object specifying pagination and sorting parameters.
     *                  Defaults to sorting by creation date in descending order if not specified.
     * @return ResponseEntity containing a PageableDto of ProductResponseDto,
     *         representing the paginated list of products.
     */
    @GetMapping
    public ResponseEntity<PageableDto<ProductResponseDto>> getAll(@RequestParam(required = false) String name,
                                                  @AllowedSortFields(values = {"name","quantity","price","createdAt"})
                                                  @PageableDefault(sort = "createdAt", direction = DESC)
                                                  Pageable pageable){
        return ResponseEntity.ok(productService.getAll(pageable,name));
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id The unique identifier of the product to retrieve.
     * @return ResponseEntity containing a ProductResponseDto representing the retrieved product.
     * @throws ProductNotFoundException if the product with the specified id is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    public PageableDto<ProductResponseDto> create(){
        return null;
    }

    @PutMapping("/{id}")
    public PageableDto<ProductResponseDto> update(@PathVariable Long id){
        return null;
    }

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id The unique identifier of the product to delete.
     * @return ResponseEntity with HTTP status 204 (NO_CONTENT) indicating successful deletion.
     * @throws ProductNotFoundException if the product with the specified id is not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDto> delete(@PathVariable Long id){
        productService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
