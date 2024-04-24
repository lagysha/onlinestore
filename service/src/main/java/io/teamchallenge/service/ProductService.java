package io.teamchallenge.service;

import io.teamchallenge.constant.ExceptionMessage;
import io.teamchallenge.dto.PageableDto;
import io.teamchallenge.dto.ProductCreateRequestDto;
import io.teamchallenge.dto.ProductResponseDto;
import io.teamchallenge.dto.ShortProductResponseDto;
import io.teamchallenge.entity.Image;
import io.teamchallenge.entity.Product;
import io.teamchallenge.entity.attributes.ProductAttribute;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.*;
import java.util.List;
import java.util.function.ToDoubleBiFunction;
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
    private final BrandRepository brandRepository;
    private final AttributeRepository attributeRepository;
    private final AttribteValueRepository attribteValueRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final CategoryRepostiory categoryRepostiory;
    private final ModelMapper modelMapper;

    /**
     * Retrieves a pageable list of products based on optional filtering by name and pageable parameters.
     *
     * @param pageable Pageable object specifying pagination and sorting parameters.
     * @param name     Optional parameter for filtering products by name.
     * @return PageableDto containing a list of ProductResponseDto representing the paginated list of products.
     */
    public PageableDto<ShortProductResponseDto> getAll(Pageable pageable, String name) {
        Page<Long> retrievedProductsIds = productRepository.findAllProductsIdByName(pageable, name);
        List<ShortProductResponseDto> content = productRepository
            .findAllByIdWithImages(retrievedProductsIds.getContent())
            .stream()
            .map(product -> modelMapper.map(product,ShortProductResponseDto.class))
            .collect(Collectors.toList());
        return new PageableDto<>(
            content,
            retrievedProductsIds.getTotalElements(),
            retrievedProductsIds.getPageable().getPageNumber(),
            retrievedProductsIds.getTotalPages());
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

    @Transactional
    public ProductResponseDto create(ProductCreateRequestDto productCreateRequestDto) {
        var brand = brandRepository.findById(productCreateRequestDto.getBrandId()).orElseThrow();
        var category = categoryRepostiory.findById(productCreateRequestDto.getCategoryId()).orElseThrow();
        var product = Product.builder()
                .name(productCreateRequestDto.getName())
                    .price(productCreateRequestDto.getPrice())
                        .brand(brand)
                            .category(category)
                                .price(productCreateRequestDto.getPrice())
                                    .quantity(productCreateRequestDto.getQuantity())
                                        .build();
        productCreateRequestDto.getImageLinks()
            .forEach(link -> product
                .addImage(Image.builder().link(link).build()));

        //TODO : verify somehow that product does not have duplicate attributes
        //TODO : basically we need to persist product before validating its attributes
        productCreateRequestDto.getProductAttributeRequestDtos()
            .forEach(attributeRequestDto -> {

                product.addProductAttribute(ProductAttribute
                    .builder()
                        .attributeValue(attribteValueRepository
                            .findById(attributeRequestDto.getAttributeValueId())
                            .get())
                    .build());
            });

        return modelMapper.map(product,ProductResponseDto.class);
    }
}
