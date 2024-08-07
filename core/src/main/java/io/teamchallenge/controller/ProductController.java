package io.teamchallenge.controller;

import io.teamchallenge.annotation.AllowedSortFields;
import io.teamchallenge.annotation.ImageValidation;
import io.teamchallenge.dto.filter.ProductFilterDto;
import io.teamchallenge.dto.pageable.AdvancedPageableDto;
import io.teamchallenge.dto.product.ProductRequestDto;
import io.teamchallenge.dto.product.ProductResponseDto;
import io.teamchallenge.dto.product.ShortProductResponseDto;
import io.teamchallenge.service.impl.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Controller for products.
 * @author Niktia Malov
 */
@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;

    /**
     * Retrieves a paginated list of products based on the provided filter criteria and pagination settings.
     *
     * @param productFilterDto  DTO containing filter criteria for products.
     * @param pageable          Pageable object for pagination and sorting information.
     *                          Allowed sort fields: "price".
     *                          Default sort: "price" in descending order.
     * @return ResponseEntity containing a pageable list of short product responses.
     */
    @GetMapping
    public ResponseEntity<AdvancedPageableDto<ShortProductResponseDto>> getAll(
        @Valid ProductFilterDto productFilterDto,
        @AllowedSortFields(values = {"price","popularity","rating"})
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
     * Creates a new product with the provided product details and images.
     *
     * @param images            List of image files to be associated with the product.
     *                          Must pass custom @ImageValidation.
     * @param productRequestDto DTO containing product details for creation.
     *                          Must be valid as per validation constraints.
     * @return ResponseEntity containing the created product details with a status of CREATED (201).
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ProductResponseDto> create(
        @RequestPart @ImageValidation List<MultipartFile> images,
        @RequestPart @Valid ProductRequestDto productRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(productService.create(productRequestDto, images));
    }

    /**
     * Updates an existing product with the provided product details and optional images.
     *
     * @param id                The ID of the product to be updated.
     * @param images            Optional list of image files to be associated with the product.
     *                          Must pass custom @ImageValidation if provided.
     * @param productRequestDto DTO containing updated product details.
     *                          Must be valid as per validation constraints.
     * @return ResponseEntity containing the updated product details with a status of OK (200).
     */
    @PutMapping(path = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id,
                                                     @RequestPart(required = false) @ImageValidation
                                                     List<MultipartFile> images,
                                                     @RequestPart @Valid ProductRequestDto productRequestDto) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(productService.update(id, productRequestDto, images));
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