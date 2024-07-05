package io.teamchallenge.service.impl;

import io.teamchallenge.constant.ExceptionMessage;
import io.teamchallenge.dto.brand.BrandRequestDto;
import io.teamchallenge.dto.brand.BrandResponseDto;
import io.teamchallenge.entity.Brand;
import io.teamchallenge.exception.DeletionException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.BrandRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.teamchallenge.constant.ExceptionMessage.BRAND_DELETION_EXCEPTION_MESSAGE;

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

    /**
     * Deletes a brand by its ID.
     *
     * <p>This method retrieves a brand by its ID and deletes it from the repository. If the brand
     * is not found, a {@link NotFoundException} is thrown. If there is a data integrity violation during
     * the deletion process, a {@link DeletionException} is thrown.
     *
     * @param brandId the ID of the brand to delete
     * @throws NotFoundException if the brand with the specified ID is not found
     * @throws DeletionException if a data integrity violation occurs during deletion
     */
    @Transactional
    public void deleteById(Long brandId) {
        var retrievedBrand = brandRepository
            .findById(brandId)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.BRAND_NOT_FOUND_BY_ID.formatted(brandId)));

        try {
            brandRepository.deleteById(retrievedBrand.getId());
        } catch (DataIntegrityViolationException e) {
            throw new DeletionException(BRAND_DELETION_EXCEPTION_MESSAGE, e);
        }
    }

    /**
     * Updates a brand by its ID with the provided update details.
     *
     * <p>This method retrieves a brand by its ID and updates its name with the value provided in the
     * {@code brandRequestDto}. If the brand is not found, a {@link NotFoundException} is thrown.
     *
     * @param brandId the ID of the brand to update
     * @param brandRequestDto the DTO containing the update details
     * @return a {@link BrandResponseDto} containing the updated brand details
     * @throws NotFoundException if the brand with the specified ID is not found
     */
    @Transactional
    public BrandResponseDto update(Long brandId, BrandRequestDto brandRequestDto) {
        var retrievedBrand = brandRepository
            .findById(brandId)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.BRAND_NOT_FOUND_BY_ID.formatted(brandId)));

        retrievedBrand.setName(brandRequestDto.getName());

        return BrandResponseDto.builder()
            .id(retrievedBrand.getId())
            .name(retrievedBrand.getName())
            .build();
    }
}
