package io.teamchallenge.service;

import io.teamchallenge.dto.PageableDto;
import io.teamchallenge.dto.ProductResponseDto;
import io.teamchallenge.entity.Product;
import io.teamchallenge.mapper.ProductResponseDtoMapper;
import io.teamchallenge.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductResponseDtoMapper productResponseDtoMapper;

    public PageableDto<ProductResponseDto> getAll(Pageable pageable,String name){
        Page<Product> retrievedProducts = productRepository.findAllBy(pageable,name);

        return new PageableDto<>(
            retrievedProducts.getContent().stream()
            .map(productResponseDtoMapper::convertToDto)
            .toList(),
            retrievedProducts.getTotalElements(),
            retrievedProducts.getPageable().getPageNumber(),
            retrievedProducts.getTotalPages());
    }
}
