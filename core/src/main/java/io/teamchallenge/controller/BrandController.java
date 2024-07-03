package io.teamchallenge.controller;

import io.teamchallenge.dto.brand.BrandRequestDto;
import io.teamchallenge.dto.brand.BrandResponseDto;
import io.teamchallenge.service.impl.BrandService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for brands.
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
    public ResponseEntity<BrandResponseDto> create(@RequestBody BrandRequestDto brandRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(brandService.create(brandRequestDto));
    }

    /**
     * Retrieves all brands.
     *
     * @return a {@link ResponseEntity} containing a list of all brands and HTTP status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<BrandResponseDto>> getAll(){
        return ResponseEntity.ok(brandService.findAll());
    }

    //TODO: endpoint to delete Attribute
}
