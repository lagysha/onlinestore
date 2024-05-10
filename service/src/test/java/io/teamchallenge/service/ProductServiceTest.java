package io.teamchallenge.service;

import io.teamchallenge.constant.ExceptionMessage;
import io.teamchallenge.dto.*;
import io.teamchallenge.entity.Brand;
import io.teamchallenge.entity.Category;
import io.teamchallenge.entity.Image;
import io.teamchallenge.entity.Product;
import io.teamchallenge.entity.attributes.Attribute;
import io.teamchallenge.entity.attributes.AttributeValue;
import io.teamchallenge.entity.attributes.ProductAttribute;
import io.teamchallenge.exception.AlreadyExistsException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.exception.PersistenceException;
import io.teamchallenge.repository.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private BrandRepository brandRepository;
    @Mock
    private AttributeValueRepository attributeValueRepository;
    @Mock
    private ProductAttributeRepository productAttributeRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private ProductService productService;

    @Test
    void getAllTest() {
        PageRequest pageable = PageRequest.of(0, 1);
        List<Long> productIds = List.of(1L);
        PageImpl<Long> retrievedIds = new PageImpl<>(productIds, pageable, 1);
        var product = getProduct();
        ShortProductResponseDto shortProductResponseDto = getShortProductResponseDto();
        var expected = new PageableDto<>(
            List.of(shortProductResponseDto),
            retrievedIds.getTotalElements(),
            retrievedIds.getPageable().getPageNumber(),
            retrievedIds.getTotalPages());
        when(productRepository.findAllIdsByName(pageable, "name"))
            .thenReturn(retrievedIds);
        when(productRepository.findAllByIdWithImages(productIds))
            .thenReturn(List.of(product));
        when(modelMapper.map(product, ShortProductResponseDto.class))
            .thenReturn(shortProductResponseDto);

        var actual = productService.getAll(pageable, "name");

        verify(productRepository).findAllIdsByName(eq(pageable), eq("name"));
        verify(productRepository).findAllByIdWithImages(eq(productIds));
        assertEquals(actual, expected);
    }

    @Test
    void getByIdTest() {
        var product = getProduct();
        var productResponseDto = getProductResponseDto();
        when(productRepository.findByIdWithCollections(1L))
            .thenReturn(Optional.of(product));
        when(modelMapper.map(product, ProductResponseDto.class))
            .thenReturn(productResponseDto);

        var actual = productService.getById(1L);

        verify(productRepository).findByIdWithCollections(eq(1L));
        assertEquals(actual, productResponseDto);
    }

    @Test
    void getByIdThrowsNotFoundExceptionTest() {
        when(productRepository.findByIdWithCollections(2L))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getById(2L),
            ExceptionMessage.PRODUCT_NOT_FOUND_BY_ID.formatted(2L));
        verify(productRepository).findByIdWithCollections(eq(2L));
    }

    @Test
    void deleteByIdTest() {
        var product = getProduct();
        when(productRepository.findById(1L))
            .thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteById(1L);

        verify(productRepository).findById(eq(1L));
        verify(productRepository).deleteById(eq(1L));
    }

    @Test
    void deleteByIdThrowsNotFoundExceptionTest() {
        when(productRepository.findById(2L))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.deleteById(2L),
            ExceptionMessage.PRODUCT_NOT_FOUND_BY_ID.formatted(2L));
        verify(productRepository).findById(eq(2L));
    }

    @Test
    void createTest() {
        var product = getProduct();
        product.setId(null);
        var savedProduct = getProduct();
        var productRequestDto = getProductRequestDto();
        var productResponseDto = getProductResponseDto();
        when(productRepository.save(product))
            .thenReturn(savedProduct);
        when(productRepository.findByName(product.getName()))
            .thenReturn(Optional.empty());
        when(brandRepository.findById(1L)).
            thenReturn(Optional.of(product.getBrand()));
        when(categoryRepository.findById(1L)).
            thenReturn(Optional.of(product.getCategory()));
        when(attributeValueRepository.getReferenceById(1L))
            .thenReturn(getAttributeValue());
        when(productAttributeRepository.findAllByIdIn(productRequestDto.getAttributeValueId()))
            .thenReturn(product.getProductAttributes());
        when(modelMapper.map(savedProduct, ProductResponseDto.class))
            .thenReturn(productResponseDto);

        var actual = productService.create(productRequestDto);

        verify(brandRepository).findById(eq(1L));
        verify(categoryRepository).findById(eq(1L));
        verify(productRepository).findByName(eq(product.getName()));
        verify(productRepository).save(eq(product));
        verify(productRepository).findByName(eq(product.getName()));
        verify(attributeValueRepository).getReferenceById(eq(1L));
        verify(productAttributeRepository).findAllByIdIn(eq(productRequestDto.getAttributeValueId()));
        assertEquals(productResponseDto, actual);
    }

    @Test
    void createThrowsNotFoundExceptionWhenBrandIdNotExistsTest() {
        when(brandRepository.findById(1L))
            .thenReturn(Optional.empty());
        var productRequestDto = getProductRequestDto();

        assertThrows(NotFoundException.class, () -> productService.create(productRequestDto),
            ExceptionMessage.BRAND_NOT_FOUND_BY_ID.formatted(1L));
        verify(brandRepository).findById(eq(1L));
    }

    @Test
    void createThrowsNotFoundExceptionWhenCategoryIdNotExistsTest() {
        when(brandRepository.findById(1L))
            .thenReturn(Optional.of(getBrand()));
        when(categoryRepository.findById(1L)).
            thenReturn(Optional.empty());
        var productRequestDto = getProductRequestDto();

        assertThrows(NotFoundException.class, () -> productService.create(productRequestDto),
            ExceptionMessage.CATEGORY_NOT_FOUND_BY_ID.formatted(1L));
        verify(brandRepository).findById(eq(1L));
        verify(categoryRepository).findById(eq(1L));
    }

    @Test
    void createThrowsAlreadyExistsExceptionWhenProductNameExistsTest() {
        var product = getProduct();
        when(brandRepository.findById(1L))
            .thenReturn(Optional.of(getBrand()));
        when(categoryRepository.findById(1L)).
            thenReturn(Optional.of(getCategory()));
        when(productRepository.findByName(product.getName()))
            .thenReturn(Optional.of(product));
        var productRequestDto = getProductRequestDto();

        assertThrows(AlreadyExistsException.class, () -> productService.create(productRequestDto),
            ExceptionMessage.PRODUCT_WITH_NAME_ALREADY_EXISTS.formatted(product.getName()));
        verify(brandRepository).findById(eq(1L));
        verify(categoryRepository).findById(eq(1L));
        verify(productRepository).findByName(eq(product.getName()));
    }

    @Test
    void createThrowsPersistenceExceptionWhenProductWithIncorrectAttributesTest() {
        var product = getProduct();
        product.setId(null);
        Long duplicatedOrNonExistingAttributeValueId = 1L;
        var savedProduct = getProduct();
        savedProduct.setId(1L);
        var productRequestDto = getProductRequestDto();
        productRequestDto.setAttributeValueId(List.of(duplicatedOrNonExistingAttributeValueId));
        when(productRepository.save(product))
            .thenThrow(DataIntegrityViolationException.class);
        when(productRepository.findByName(product.getName()))
            .thenReturn(Optional.empty());
        when(brandRepository.findById(1L)).
            thenReturn(Optional.of(product.getBrand()));
        when(categoryRepository.findById(1L)).
            thenReturn(Optional.of(product.getCategory()));
        when(attributeValueRepository.getReferenceById(1L))
            .thenReturn(getAttributeValue());

        assertThrows(PersistenceException.class, () -> productService.create(productRequestDto),
            ExceptionMessage.PRODUCT_PERSISTENCE_EXCEPTION);

        verify(brandRepository).findById(eq(1L));
        verify(categoryRepository).findById(eq(1L));
        verify(productRepository).findByName(eq(product.getName()));
        verify(productRepository).save(eq(product));
        verify(productRepository).findByName(eq(product.getName()));
        verify(attributeValueRepository).getReferenceById(eq(1L));
    }

    @Test
    void updateTest() {
        var product = getProduct();
        var productRequestDto = getProductRequestDto();
        var productResponseDto = getProductResponseDto();
        when(productRepository.findByIdWithCollections(1L))
            .thenReturn(Optional.of(getProduct()));
        when(brandRepository.findById(1L)).
            thenReturn(Optional.of(product.getBrand()));
        when(categoryRepository.findById(1L)).
            thenReturn(Optional.of(product.getCategory()));
        when(productRepository.findByNameAndIdNot(productRequestDto.getName(), 1L))
            .thenReturn(Optional.empty());
        when(attributeValueRepository.findAllByIdIn(Collections.emptyList()))
            .thenReturn(Collections.emptyList());
        when(modelMapper.map(product, ProductResponseDto.class))
            .thenReturn(productResponseDto);

        var actual = productService.update(1L, productRequestDto);

        verify(brandRepository).findById(eq(1L));
        verify(categoryRepository).findById(eq(1L));
        verify(productRepository).findByIdWithCollections(eq(1L));
        verify(attributeValueRepository).findAllByIdIn(eq(Collections.emptyList()));
        verify(productRepository).findByNameAndIdNot(eq(productRequestDto.getName()), eq(1L));
        assertEquals(productResponseDto, actual);
    }

    @Test
    void updateThrowsNotFoundExceptionWhenBrandIdNotExistsTest() {
        var productRequestDto = getProductRequestDto();
        when(productRepository.findByIdWithCollections(1L))
            .thenReturn(Optional.of(getProduct()));
        when(brandRepository.findById(1L))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.update(1L, productRequestDto),
            ExceptionMessage.BRAND_NOT_FOUND_BY_ID.formatted(1L));
        verify(brandRepository).findById(eq(1L));
        verify(productRepository).findByIdWithCollections(eq(1L));
    }

    @Test
    void updateThrowsNotFoundExceptionWhenCategoryIdNotExistsTest() {
        var productRequestDto = getProductRequestDto();
        when(productRepository.findByIdWithCollections(1L))
            .thenReturn(Optional.of(getProduct()));
        when(brandRepository.findById(1L))
            .thenReturn(Optional.of(getBrand()));
        when(categoryRepository.findById(1L)).
            thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.update(1L, productRequestDto),
            ExceptionMessage.CATEGORY_NOT_FOUND_BY_ID.formatted(1L));
        verify(brandRepository).findById(eq(1L));
        verify(categoryRepository).findById(eq(1L));
        verify(productRepository).findByIdWithCollections(eq(1L));
    }

    @Test
    void updateThrowsNotFoundExceptionWhenProductIdNotExistsTest() {
        var product = getProduct();
        var productRequestDto = getProductRequestDto();
        when(productRepository.findByIdWithCollections(1L))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.update(1L, productRequestDto),
            ExceptionMessage.PRODUCT_NOT_FOUND_BY_ID.formatted(product.getName()));
        verify(productRepository).findByIdWithCollections(eq(1L));
    }

    @Test
    void updateThrowsAlreadyExistsExceptionWhenProductNameExistsTest() {
        var productRequestDto = getProductRequestDto();
        var product = getProduct();
        when(productRepository.findByIdWithCollections(1L))
            .thenReturn(Optional.of(getProduct()));
        when(brandRepository.findById(1L))
            .thenReturn(Optional.of(getBrand()));
        when(categoryRepository.findById(1L)).
            thenReturn(Optional.of(getCategory()));
        when(productRepository.findByNameAndIdNot(productRequestDto.getName(), 1L))
            .thenReturn(Optional.of(product));

        assertThrows(AlreadyExistsException.class, () -> productService.update(1L, productRequestDto),
            ExceptionMessage.PRODUCT_WITH_NAME_ALREADY_EXISTS.formatted(product.getName()));
        verify(brandRepository).findById(eq(1L));
        verify(categoryRepository).findById(eq(1L));
        verify(productRepository).findByNameAndIdNot(eq(product.getName()), eq(1L));
        verify(productRepository).findByIdWithCollections(eq(1L));
    }

    @Test
    void updateThrowsPersistenceExceptionWhenProductWithIncorrectAttributesTest() {
        var product = getProduct();
        var productRequestDto = getProductRequestDto();
        productRequestDto.setAttributeValueId(List.of(3L, 1L));
        AttributeValue attributeValue = getAttributeValue();
        attributeValue.setId(3L);
        when(productRepository.findByIdWithCollections(1L))
            .thenReturn(Optional.of(getProduct()));
        when(brandRepository.findById(1L)).
            thenReturn(Optional.of(product.getBrand()));
        when(categoryRepository.findById(1L)).
            thenReturn(Optional.of(product.getCategory()));
        when(productRepository.findByNameAndIdNot(productRequestDto.getName(), 1L))
            .thenReturn(Optional.empty());
        when(attributeValueRepository.getReferenceById(3L))
            .thenReturn(attributeValue);
        when(attributeValueRepository.findAllByIdIn(List.of(3L)))
            .thenThrow(DataIntegrityViolationException.class);

        assertThrows(PersistenceException.class, () -> productService.update(1L, productRequestDto),
            ExceptionMessage.PRODUCT_PERSISTENCE_EXCEPTION);
        verify(productRepository).findByIdWithCollections(eq(1L));
        verify(brandRepository).findById(eq(1L));
        verify(categoryRepository).findById(eq(1L));
        verify(productRepository).findByNameAndIdNot(eq(productRequestDto.getName()), eq(1L));
        verify(attributeValueRepository).getReferenceById(eq(3L));
        verify(attributeValueRepository).findAllByIdIn(eq(List.of(3L)));
    }

    private Category getCategory() {
        return Category.builder()
            .id(1L)
            .name("name1")
            .build();
    }

    private Product getProduct() {
        List<ProductAttribute> productAttributes = new ArrayList<>();
        productAttributes.add(getProductAttribute());
        List<Image> images = new ArrayList<>();
        images.add(getImage());
        return Product
            .builder()
            .id(1L)
            .shortDesc("shortDesc")
            .name("name")
            .category(getCategory())
            .productAttributes(productAttributes)
            .price(BigDecimal.ONE)
            .images(images)
            .brand(getBrand())
            .description("desc")
            .quantity(1)
            .build();
    }

    private Image getImage() {
        return Image.builder()
            .link("https://image.jpg").build();
    }

    private ShortProductResponseDto getShortProductResponseDto() {
        var product = getProduct();
        return ShortProductResponseDto.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .images(product.getImages().stream()
                .map(Image::getLink)
                .collect(Collectors.toList()))
            .build();
    }

    private ProductResponseDto getProductResponseDto() {
        var product = getProduct();
        return ProductResponseDto.builder()
            .id(product.getId())
            .shortDesc(product.getShortDesc())
            .categoryResponseDto(
                CategoryResponseDto.builder()
                    .desc(product.getCategory().getDescription())
                    .name(product.getCategory().getName())
                    .build())
            .productAttributeResponseDtos(product.getProductAttributes()
                .stream()
                .map((pa) -> new ProductAttributeResponseDto(
                    pa.getAttributeValue().getAttribute().getName(),
                    pa.getAttributeValue().getValue()))
                .collect(Collectors.toList()))
            .images(product.getImages()
                .stream()
                .map(Image::getLink)
                .collect(Collectors.toList()))
            .brand(product.getBrand().getName())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .quantity(product.getQuantity())
            .createdAt(product.getCreatedAt())
            .build();
    }

    private ProductRequestDto getProductRequestDto() {
        return ProductRequestDto
            .builder()
            .shortDesc("shortDesc")
            .categoryId(1L)
            .attributeValueId(List.of(1L))
            .imageLinks(List.of("https://image.jpg"))
            .brandId(1L)
            .name("name")
            .description("desc")
            .price(BigDecimal.ONE)
            .quantity(1)
            .build();
    }

    private AttributeValue getAttributeValue() {
        return AttributeValue.builder()
            .id(1L)
            .attribute(getAttribute())
            .value("value")
            .build();
    }

    private Attribute getAttribute() {
        return Attribute.builder().id(1L).name("name").build();
    }

    private ProductAttribute getProductAttribute() {
        return ProductAttribute.builder()
            .attributeValue(getAttributeValue())
            .build();
    }

    private Brand getBrand() {
        return Brand.builder()
            .id(1L)
            .name("name1")
            .build();
    }
}