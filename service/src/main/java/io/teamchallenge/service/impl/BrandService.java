package io.teamchallenge.service.impl;

import io.teamchallenge.dto.brand.BrandRequestDto;
import io.teamchallenge.dto.brand.BrandResponseDto;
import io.teamchallenge.entity.Brand;
import io.teamchallenge.repository.BrandRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing brands.
 * @author Niktia Malov
 */
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    /**
     * Creates a new brand.
     *
     * @param brandRequestDto the DTO containing the details of the brand to create
     * @return a {@link BrandResponseDto} containing the details of the created brand
     */
    @Transactional
    public BrandResponseDto create(BrandRequestDto brandRequestDto) {
        var savedBrand = brandRepository.save(Brand.builder()
            .name(brandRequestDto.getName())
            .build());

        return BrandResponseDto.builder()
            .id(savedBrand.getId())
            .name(savedBrand.getName())
            .build();
    }

    /**
     * Retrieves all brands.
     *
     * @return a list of {@link BrandResponseDto} containing the details of all brands
     */
    public List<BrandResponseDto> findAll() {
        return brandRepository.findAll()
            .stream()
            .map(brand -> BrandResponseDto.builder()
            .id(brand.getId())
            .name(brand.getName())
            .build())
            .collect(Collectors.toList());
    }
}
