package io.teamchallenge.controller;

import io.teamchallenge.dto.brand.BrandRequestDto;
import io.teamchallenge.dto.brand.BrandResponseDto;
import io.teamchallenge.service.impl.BrandService;
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
 * Controller for brands.
 *
 * @author Niktia Malov
 */
@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    /**
     * Creates a new brand.
     *
     * @param brandRequestDto the DTO containing the details of the brand to create
     * @return a {@link ResponseEntity} containing the created brand and HTTP status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<BrandResponseDto> create(@RequestBody BrandRequestDto brandRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(brandService.create(brandRequestDto));
    }

    /**
     * Retrieves all brands.
     *
     * @return a {@link ResponseEntity} containing a list of all brands and HTTP status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<BrandResponseDto>> getAll() {
        return ResponseEntity.ok(brandService.findAll());
    }

    /**
     * Deletes a brand by its ID.
     *
     * <p>This method calls the {@code brandService} to delete a brand by its ID. If the deletion is successful,
     * it returns a response with HTTP status 204 (No Content).
     *
     * @param id the ID of the brand to delete
     * @return a {@link ResponseEntity} with HTTP status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        brandService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Updates a brand by its ID with the provided update details.
     *
     * <p>This method calls the {@code brandService} to update a brand's details using a PUT request.
     * It returns the updated brand details wrapped in a {@link ResponseEntity} with HTTP status 200 (OK).
     *
     * @param id the ID of the brand to update
     * @param brandRequestDto the DTO containing the update details
     * @return a {@link ResponseEntity} containing the updated brand details with HTTP status 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<BrandResponseDto> patchUpdate(@PathVariable("id") Long id,
                                                        @RequestBody BrandRequestDto brandRequestDto) {
        return ResponseEntity.ok(brandService.update(id, brandRequestDto));
    }
}
