package io.teamchallenge.controller;

import io.teamchallenge.annatation.AllowedSortFields;
import io.teamchallenge.dto.PageableDto;
import io.teamchallenge.dto.ProductResponseDto;
import io.teamchallenge.dto.ShortProductResponseDto;
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
    public ResponseEntity<PageableDto<ShortProductResponseDto>> getAll(@RequestParam(required = false) String name,
                                                                       @AllowedSortFields(values = {"name","quantity","price","createdAt"})
                                                  @PageableDefault(sort = "createdAt", direction = DESC)
                                                  Pageable pageable){
        return ResponseEntity.ok(productService.getAll(pageable,name));
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDto> delete(@PathVariable Long id){
        productService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
