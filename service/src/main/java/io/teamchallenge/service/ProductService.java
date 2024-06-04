package io.teamchallenge.service;

import io.teamchallenge.constant.ExceptionMessage;
import io.teamchallenge.dto.filter.ProductFilterDto;
import io.teamchallenge.dto.pageable.AdvancedPageableDto;
import io.teamchallenge.dto.product.ProductMinMaxPriceDto;
import io.teamchallenge.dto.product.ProductRequestDto;
import io.teamchallenge.dto.product.ProductResponseDto;
import io.teamchallenge.dto.product.ShortProductResponseDto;
import io.teamchallenge.entity.Brand;
import io.teamchallenge.entity.Category;
import io.teamchallenge.entity.Image;
import io.teamchallenge.entity.Product;
import io.teamchallenge.entity.attributes.ProductAttribute;
import io.teamchallenge.exception.AlreadyExistsException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.exception.PersistenceException;
import io.teamchallenge.repository.AttributeValueRepository;
import io.teamchallenge.repository.BrandRepository;
import io.teamchallenge.repository.CategoryRepository;
import io.teamchallenge.repository.ProductAttributeRepository;
import io.teamchallenge.repository.ProductRepository;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.teamchallenge.constant.ExceptionMessage.BRAND_NOT_FOUND_BY_ID;
import static io.teamchallenge.constant.ExceptionMessage.CATEGORY_NOT_FOUND_BY_ID;
import static io.teamchallenge.constant.ExceptionMessage.PRODUCT_PERSISTENCE_EXCEPTION;
import static io.teamchallenge.constant.ExceptionMessage.PRODUCT_WITH_NAME_ALREADY_EXISTS;
import static io.teamchallenge.repository.ProductRepository.Specs.byAttributeValuesIds;
import static io.teamchallenge.repository.ProductRepository.Specs.byBrandIds;
import static io.teamchallenge.repository.ProductRepository.Specs.byCategoryId;
import static io.teamchallenge.repository.ProductRepository.Specs.byName;
import static io.teamchallenge.repository.ProductRepository.Specs.byPrice;

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
    public AdvancedPageableDto<ShortProductResponseDto> getAll(Pageable pageable, ProductFilterDto productFilterDto) {
        Specification<Product> specification = null;
        Sort sort = pageable.getSort();
        Page<Long> retrievedProducts;
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        if (areAllVariablesNull(productFilterDto)) {
            retrievedProducts =
                productRepository.findAllProductIds(null,pageable);
        } else {
            specification = getSpecificationFromFilterDto(productFilterDto);
            retrievedProducts =
                productRepository.findAllProductIds(specification, pageable);
        }

        List<ShortProductResponseDto> content = productRepository
            .findAllByIdWithImages(retrievedProducts
                    .getContent(),
                sort)
            .stream()
            .map(product -> modelMapper.map(product, ShortProductResponseDto.class))
            .collect(Collectors.toList());

        ProductMinMaxPriceDto productMinMaxPriceDto;
        if(Objects.isNull(productFilterDto.getPrice())) {
            productMinMaxPriceDto = productRepository.getProductMinMaxPrice(specification);
        }else{
            productMinMaxPriceDto = new ProductMinMaxPriceDto(BigDecimal.valueOf(productFilterDto.getPrice().getFrom()),
                BigDecimal.valueOf(productFilterDto.getPrice().getTo()));
        }

        return AdvancedPageableDto.<ShortProductResponseDto>builder()
            .page(content)
            .totalElements(retrievedProducts.getNumberOfElements())
            .currentPage(retrievedProducts.getPageable().getPageNumber())
            .totalPages(retrievedProducts.getTotalPages())
            .minPrice(productMinMaxPriceDto.getMin())
            .maxPrice(productMinMaxPriceDto.getMax())
            .build();
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
    @Transactional
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
     * @throws PersistenceException   if there is an issue creating the product.
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
     * @throws PersistenceException   if there is an issue creating the product.
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

        product.clearAllImages();
        productRequestDto.getImageLinks()
            .forEach(link -> product
                .addImage(Image.builder().link(link).build()));

        try {
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

    private Specification<Product> getSpecificationFromFilterDto(ProductFilterDto productFilterDto) {
        List<Specification<Product>> specifications = new ArrayList<>();
        var name = productFilterDto.getName();
        if (!Objects.isNull(name)) {
            specifications.add(byName(name));
        }
        var priceFilter = productFilterDto.getPrice();
        if (!Objects.isNull(priceFilter)) {
            specifications.add(
                byPrice(BigDecimal.valueOf(priceFilter.getFrom()), BigDecimal.valueOf(priceFilter.getTo())));
        }
        var brandIds = productFilterDto.getBrandIds();
        if (!Objects.isNull(brandIds)) {
            specifications.add(byBrandIds(brandIds));
        }
        var categoryId = productFilterDto.getCategoryId();
        if (!Objects.isNull(categoryId)) {
            specifications.add(byCategoryId(categoryId));
        }
        var attributeValueIds = productFilterDto.getAttributeValueIds();
        if (!Objects.isNull(attributeValueIds)) {
            specifications.add(byAttributeValuesIds(attributeValueIds));
        }
        return Specification.allOf(specifications);
    }

    public boolean areAllVariablesNull(@NotNull ProductFilterDto obj) {
        return Arrays.stream(obj.getClass().getDeclaredFields())
            .peek(field -> field.setAccessible(true))
            .allMatch(field -> {
                try {
                    return field.get(obj) == null;
                } catch (IllegalAccessException e) {
                    return false;
                }
            });
    }
}
