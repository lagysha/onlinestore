package io.teamchallenge.service;

import io.teamchallenge.constant.ExceptionMessage;
import io.teamchallenge.dto.PageableDto;
import io.teamchallenge.dto.ProductResponseDto;
import io.teamchallenge.dto.ShortProductResponseDto;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    /**
     * Retrieves a pageable list of products based on optional filtering by name and pageable parameters.
     *
     * @param pageable Pageable object specifying pagination and sorting parameters.
     * @param name     Optional parameter for filtering products by name.
     * @return PageableDto containing a list of ProductResponseDto representing the paginated list of products.
     */
    public PageableDto<ShortProductResponseDto> getAll(Pageable pageable, String name) {
        Page<Long> retrievedProducts = productRepository.findAllProductsIdByName(pageable, name);
        List<ShortProductResponseDto> content = productRepository
            .findAllByIdWithImages(retrievedProducts.getContent())
            .stream()
            .map(product -> modelMapper.map(product,ShortProductResponseDto.class))
            .collect(Collectors.toList());
        return new PageableDto<>(
            content,
            retrievedProducts.getTotalElements(),
            retrievedProducts.getPageable().getPageNumber(),
            retrievedProducts.getTotalPages());
    }

    public ProductResponseDto getById(Long id) {
        var retrievedProduct = productRepository
            .findByIdWithCategoryAndBrandAndProductAttribute(id)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND_BY_ID.formatted(id)));
        return productRepository.findByNameWithImages(retrievedProduct.getId())
            .stream()
            .map(product -> modelMapper.map(product,ProductResponseDto.class))
            .findAny()
            .get();
    }

    public void deleteById(Long id) {
        var retrievedProduct = productRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND_BY_ID.formatted(id)));
        productRepository.deleteById(retrievedProduct.getId());
    }
}
