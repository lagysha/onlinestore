package io.teamchallenge.service;

import io.teamchallenge.constant.ExceptionMessage;
import static io.teamchallenge.constant.ExceptionMessage.*;
import io.teamchallenge.dto.PageableDto;
import io.teamchallenge.dto.ProductRequestDto;
import io.teamchallenge.dto.ProductResponseDto;
import io.teamchallenge.dto.ShortProductResponseDto;
import io.teamchallenge.entity.Brand;
import io.teamchallenge.entity.Category;
import io.teamchallenge.entity.Image;
import io.teamchallenge.entity.Product;
import io.teamchallenge.entity.attributes.ProductAttribute;
import io.teamchallenge.exception.AlreadyExistsException;
import io.teamchallenge.exception.PersistenceException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final AttributeValueRepository attributeValueRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    /**
     * Retrieves a pageable list of products based on optional filtering by name and pageable parameters.
     *
     * @param pageable Pageable object specifying pagination and sorting parameters.
     * @param name     Optional parameter for filtering products by name.
     * @return PageableDto containing a list of ProductResponseDto representing the paginated list of products.
     */
    public PageableDto<ShortProductResponseDto> getAll(Pageable pageable, String name) {
        Page<Long> retrievedProducts = productRepository.findAllIdsByName(pageable, name);
        List<ShortProductResponseDto> content = productRepository
            .findAllByIdWithImages(retrievedProducts.getContent())
            .stream()
            .map(product -> modelMapper.map(product, ShortProductResponseDto.class))
            .collect(Collectors.toList());
        return new PageableDto<>(
            content,
            retrievedProducts.getTotalElements(),
            retrievedProducts.getPageable().getPageNumber(),
            retrievedProducts.getTotalPages());
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id The identifier of the product to retrieve.
     * @return The ProductResponseDto object representing the retrieved product.
     * @throws NotFoundException if the product with the given ID is not found.
     */
    public ProductResponseDto getById(Long id) {
        return productRepository
            .findByIdWithCollections(id)
            .stream()
            .map(product -> modelMapper.map(product, ProductResponseDto.class))
            .findAny()
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND_BY_ID.formatted(id)));
    }


    /**
     * Deletes a product by its unique identifier.
     *
     * @param id The identifier of the product to delete.
     * @throws NotFoundException if the product with the given ID is not found.
     */
    public void deleteById(Long id) {
        var retrievedProduct = productRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND_BY_ID.formatted(id)));
        productRepository.deleteById(retrievedProduct.getId());
    }


    /**
     * Creates a new product based on the provided ProductRequestDto.
     *
     * @param productRequestDto The ProductRequestDto containing the details of the product to create.
     * @return The ProductResponseDto representing the created product.
     * @throws PersistenceException      if there is an issue creating the product.
     * @throws NotFoundException      if the brand or category specified in the request is not found.
     * @throws AlreadyExistsException if the product name is invalid.
     */
    @Transactional
    public ProductResponseDto create(ProductRequestDto productRequestDto) {
        var brand = getBrandById(productRequestDto);
        var category = getCategoryById(productRequestDto);
        validateProductName(productRequestDto);
        var product = Product.builder()
            .name(productRequestDto.getName())
            .price(productRequestDto.getPrice())
            .brand(brand)
            .shortDesc(productRequestDto.getShortDesc())
            .description(productRequestDto.getDescription())
            .category(category)
            .quantity(productRequestDto.getQuantity())
            .images(new ArrayList<>())
            .productAttributes(new ArrayList<>())
            .build();
        productRequestDto.getImageLinks()
            .forEach(link -> product
                .addImage(Image.builder().link(link).build()));


        productRequestDto.getAttributeValueId()
            .forEach(attributeValueId ->
                product.addProductAttribute(ProductAttribute
                    .builder()
                    .attributeValue(attributeValueRepository
                        .getReferenceById(attributeValueId))
                    .build())
            );

        try {
            //TODO: Also add batch insert if needed
            var savedProduct = productRepository.save(product);
            productAttributeRepository.findAllByIdIn(productRequestDto.getAttributeValueId());

            return modelMapper.map(savedProduct, ProductResponseDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new PersistenceException(PRODUCT_PERSISTENCE_EXCEPTION, e);
        }
    }


    /**
     * Updates an existing product with the provided ID using the details from the ProductRequestDto.
     *
     * @param id                The identifier of the product to update.
     * @param productRequestDto The ProductRequestDto containing the updated details of the product.
     * @return The ProductResponseDto representing the updated product.
     * @throws PersistenceException      if there is an issue creating the product.
     * @throws NotFoundException      if the brand or category specified in the request is not found.
     * @throws AlreadyExistsException if the product name is invalid.
     */
    @Transactional
    public ProductResponseDto update(Long id, ProductRequestDto productRequestDto) {
        var product = productRepository
            .findByIdWithCollections(id)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND_BY_ID.formatted(id)));
        var brand = getBrandById(productRequestDto);
        var category = getCategoryById(productRequestDto);
        validateProductNameWhereIdNotEquals(productRequestDto, id);

        product.setBrand(brand);
        product.setCategory(category);
        product.setDescription(productRequestDto.getDescription());
        product.setName(productRequestDto.getName());
        product.setQuantity(productRequestDto.getQuantity());
        product.setShortDesc(productRequestDto.getShortDesc());
        product.setPrice(productRequestDto.getPrice());


        //TODO: add service here to store images and retrieve links
        product.clearAllImages();
        productRequestDto.getImageLinks()
            .forEach(link -> product
                .addImage(Image.builder().link(link).build()));

        try {
            //TODO: Also add batch insert and batch delete if needed
            List<Long> idsToFetch = updateProductAttributes(productRequestDto, product);
            attributeValueRepository.findAllByIdIn(idsToFetch);
            return modelMapper.map(product, ProductResponseDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new PersistenceException(PRODUCT_PERSISTENCE_EXCEPTION, e);
        }
    }


    private List<Long> updateProductAttributes(ProductRequestDto productRequestDto, Product product) {
        product.getProductAttributes()
            .stream().filter(
                productAttribute -> !productRequestDto.getAttributeValueId()
                    .contains(productAttribute.getAttributeValue().getId()))
            .toList()
            .forEach(product::removeProductAttribute);
        List<Long> productAttributeAttributeValueIds = product.getProductAttributes()
            .stream().map(productAttribute -> productAttribute.getAttributeValue().getId())
            .toList();
        List<Long> idsToFetch = new ArrayList<>();
        for (Long attributeId : productRequestDto.getAttributeValueId()) {
            if (!productAttributeAttributeValueIds.contains(attributeId)) {
                product.addProductAttribute(ProductAttribute
                    .builder()
                    .attributeValue(attributeValueRepository
                        .getReferenceById(attributeId))
                    .build());
                idsToFetch.add(attributeId);
            }
        }
        return idsToFetch;
    }

    private void validateProductName(ProductRequestDto productRequestDto) {
        var product = productRepository.findByName(productRequestDto.getName());
        if (product.isPresent()) {
            throw new AlreadyExistsException(
                PRODUCT_WITH_NAME_ALREADY_EXISTS.formatted(productRequestDto.getName()));
        }
    }

    private void validateProductNameWhereIdNotEquals(ProductRequestDto productRequestDto, Long notEqualsId) {
        var product =
            productRepository.findByNameAndIdNot(productRequestDto.getName(), notEqualsId);
        if (product.isPresent()) {
            throw new AlreadyExistsException(
                PRODUCT_WITH_NAME_ALREADY_EXISTS.formatted(productRequestDto.getName()));
        }
    }

    private Brand getBrandById(ProductRequestDto productRequestDto) {
        return brandRepository.findById(productRequestDto.getBrandId()).orElseThrow(
            () -> new NotFoundException(BRAND_NOT_FOUND_BY_ID.formatted(productRequestDto.getBrandId())));
    }

    private Category getCategoryById(ProductRequestDto productRequestDto) {
        return categoryRepository.findById(productRequestDto.getCategoryId()).orElseThrow(
            () -> new NotFoundException(CATEGORY_NOT_FOUND_BY_ID.formatted(productRequestDto.getCategoryId())));
    }
}