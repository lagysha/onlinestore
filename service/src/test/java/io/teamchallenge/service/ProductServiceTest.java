package io.teamchallenge.service;

import io.teamchallenge.constant.ExceptionMessage;
import io.teamchallenge.dto.filter.ProductFilterDto;
import io.teamchallenge.dto.product.ProductResponseDto;
import io.teamchallenge.dto.product.ShortProductResponseDto;
import io.teamchallenge.entity.Product;
import io.teamchallenge.entity.attributes.AttributeValue;
import io.teamchallenge.exception.AlreadyExistsException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.exception.PersistenceException;
import io.teamchallenge.repository.AttributeValueRepository;
import io.teamchallenge.repository.BrandRepository;
import io.teamchallenge.repository.CategoryRepository;
import io.teamchallenge.repository.ProductAttributeRepository;
import io.teamchallenge.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import static io.teamchallenge.util.Utils.PRODUCT_IMAGES_FOLDER_NAME;
import static io.teamchallenge.util.Utils.getAdvancedPageableDto;
import static io.teamchallenge.util.Utils.getAttributeValue;
import static io.teamchallenge.util.Utils.getBrand;
import static io.teamchallenge.util.Utils.getCategory;
import static io.teamchallenge.util.Utils.getProduct;
import static io.teamchallenge.util.Utils.getProductFilterDto;
import static io.teamchallenge.util.Utils.getProductMinMaxPriceDto;
import static io.teamchallenge.util.Utils.getProductRequestDto;
import static io.teamchallenge.util.Utils.getProductResponseDto;
import static io.teamchallenge.util.Utils.getShortProductResponseDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final CategoryRepository categoryRepository;
    private final ImageCloudService imageCloudService;
    private final ModelMapper modelMapper;

    private io.teamchallenge.service.ProductService productService;

    public ProductServiceTest() {
        productRepository = mock(ProductRepository.class);
        brandRepository = mock(BrandRepository.class);
        attributeValueRepository = mock(AttributeValueRepository.class);
        productAttributeRepository = mock(ProductAttributeRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        imageCloudService = mock(ImageCloudService.class);
        modelMapper = mock(ModelMapper.class);
        productService = new io.teamchallenge.service.ProductService(productRepository, brandRepository, attributeValueRepository,
            productAttributeRepository, categoryRepository, modelMapper,imageCloudService);
        ReflectionTestUtils.setField(productService, "product_images_folder_name", PRODUCT_IMAGES_FOLDER_NAME);
    }

    @Test
    void getAllTest() {
        PageRequest pageable = PageRequest.of(0, 1);
        List<Long> productIds = List.of(1L);
        PageImpl<Long> retrievedIds = new PageImpl<>(productIds, pageable, 1);
        var product = getProduct();
        var filter = getProductFilterDto();
        ShortProductResponseDto shortProductResponseDto = getShortProductResponseDto();
        var expected = getAdvancedPageableDto();
        Specification<Product> specification1 = mock(Specification.class);
        Specification<Product> specification2 = mock(Specification.class);
        Specification<Product> specification3 = mock(Specification.class);
        Specification<Product> specification4 = mock(Specification.class);
        Specification<Product> specification5 = mock(Specification.class);
        List<Specification<Product>> specifications =
            List.of(specification1, specification2, specification3, specification4, specification5);
        Specification<Product> specification =
            Specification.allOf(specifications);

        try (var mockStaticSpecification = mockStatic(Specification.class);
             var specs = mockStatic(ProductRepository.Specs.class)) {
            when(Specification.allOf((Iterable<Specification<Product>>)any())).thenReturn(specification);

            when(ProductRepository.Specs.byName(filter.getName()))
                .thenReturn(specification1);
            when(ProductRepository.Specs.byBrandIds(filter.getBrandIds()))
                .thenReturn(specification2);
            when(ProductRepository.Specs.byCategoryId(filter.getCategoryId()))
                .thenReturn(specification3);
            when(ProductRepository.Specs.byPriceRange(BigDecimal.valueOf(filter.getPrice().getFrom()),
                BigDecimal.valueOf(filter.getPrice().getTo())))
                .thenReturn(specification4);
            when(ProductRepository.Specs.byAttributeValuesIds(filter.getAttributeValueIds()))
                .thenReturn(specification5);
            when(productRepository.findAllProductIds(specification, pageable))
                .thenReturn(retrievedIds);
            when(productRepository.findAllByIdWithImages(productIds))
                .thenReturn(List.of(product));
            when(modelMapper.map(product, ShortProductResponseDto.class))
                .thenReturn(shortProductResponseDto);

            var actual = productService.getAll(pageable, filter);

            verify(productRepository).findAllProductIds(eq(specification), eq(pageable));
            verify(productRepository).findAllByIdWithImages(eq(productIds));
            verify(modelMapper).map(eq(product), eq(ShortProductResponseDto.class));
            assertEquals(expected,actual);

        }
    }

    @Test
    void getAllWithEmptyFilterTest() {
        PageRequest pageable = PageRequest.of(0, 1);
        List<Long> productIds = List.of(1L);
        PageImpl<Long> retrievedIds = new PageImpl<>(productIds, pageable, 1);
        var product = getProduct();
        ProductFilterDto filter = new ProductFilterDto();
        ShortProductResponseDto shortProductResponseDto = getShortProductResponseDto();
        var expected = getAdvancedPageableDto();
        var productMinMaxDto = getProductMinMaxPriceDto();


        when(productRepository.findAllProductIds(null, pageable))
            .thenReturn(retrievedIds);
        when(productRepository.findProductMinMaxPrice(null))
            .thenReturn(productMinMaxDto);
        when(productRepository.findAllByIdWithImages(productIds))
            .thenReturn(List.of(product));
        when(modelMapper.map(product, ShortProductResponseDto.class))
            .thenReturn(shortProductResponseDto);

        var actual = productService.getAll(pageable, filter);

        verify(productRepository).findAllProductIds(eq(null), eq(pageable));
        verify(productRepository).findProductMinMaxPrice(eq(null));
        verify(productRepository).findAllByIdWithImages(eq(productIds));
        verify(modelMapper).map(eq(product), eq(ShortProductResponseDto.class));
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
        verify(modelMapper).map(eq(product), eq(ProductResponseDto.class));
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
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.fdsf",
            "image/fdsf",
            new byte[0]
        );
        List<MultipartFile> multipartFiles = List.of(file);
        var savedProduct = getProduct();
        var productRequestDto = getProductRequestDto();
        var productResponseDto = getProductResponseDto();
//        when(imageCloudService.uploadImage(file)
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

        var actual = productService.create(productRequestDto,multipartFiles);

        verify(brandRepository).findById(eq(1L));
        verify(categoryRepository).findById(eq(1L));
        verify(productRepository).findByName(eq(product.getName()));
        verify(productRepository).save(eq(product));
        verify(productRepository).findByName(eq(product.getName()));
        verify(attributeValueRepository).getReferenceById(eq(1L));
        verify(productAttributeRepository).findAllByIdIn(eq(productRequestDto.getAttributeValueId()));
        verify(modelMapper).map(eq(savedProduct), eq(ProductResponseDto.class));
        assertEquals(productResponseDto, actual);
    }

    @Test
    void createThrowsNotFoundExceptionWhenBrandIdNotExistsTest() {
        List<MultipartFile> multipartFiles = List.of(new MockMultipartFile(
            "file",
            "test.fdsf",
            "image/fdsf",
            new byte[0]
        ));
        when(brandRepository.findById(1L))
            .thenReturn(Optional.empty());
        var productRequestDto = getProductRequestDto();

        assertThrows(NotFoundException.class, () -> productService.create(productRequestDto,multipartFiles),
            ExceptionMessage.BRAND_NOT_FOUND_BY_ID.formatted(1L));
        verify(brandRepository).findById(eq(1L));
    }

    @Test
    void createThrowsNotFoundExceptionWhenCategoryIdNotExistsTest() {
        List<MultipartFile> multipartFiles = List.of(new MockMultipartFile(
            "file",
            "test.fdsf",
            "image/fdsf",
            new byte[0]
        ));
        when(brandRepository.findById(1L))
            .thenReturn(Optional.of(getBrand()));
        when(categoryRepository.findById(1L)).
            thenReturn(Optional.empty());
        var productRequestDto = getProductRequestDto();

        assertThrows(NotFoundException.class, () -> productService.create(productRequestDto,multipartFiles),
            ExceptionMessage.CATEGORY_NOT_FOUND_BY_ID.formatted(1L));
        verify(brandRepository).findById(eq(1L));
        verify(categoryRepository).findById(eq(1L));
    }

    @Test
    void createThrowsAlreadyExistsExceptionWhenProductNameExistsTest() {
        List<MultipartFile> multipartFiles = List.of(new MockMultipartFile(
            "file",
            "test.fdsf",
            "image/fdsf",
            new byte[0]
        ));
        var product = getProduct();
        when(brandRepository.findById(1L))
            .thenReturn(Optional.of(getBrand()));
        when(categoryRepository.findById(1L)).
            thenReturn(Optional.of(getCategory()));
        when(productRepository.findByName(product.getName()))
            .thenReturn(Optional.of(product));
        var productRequestDto = getProductRequestDto();

        assertThrows(AlreadyExistsException.class, () -> productService.create(productRequestDto,multipartFiles),
            ExceptionMessage.PRODUCT_WITH_NAME_ALREADY_EXISTS.formatted(product.getName()));
        verify(brandRepository).findById(eq(1L));
        verify(categoryRepository).findById(eq(1L));
        verify(productRepository).findByName(eq(product.getName()));
    }

    @Test
    void createThrowsPersistenceExceptionWhenProductWithIncorrectAttributesTest() {
        List<MultipartFile> multipartFiles = List.of(new MockMultipartFile(
            "file",
            "test.fdsf",
            "image/fdsf",
            new byte[0]
        ));
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

        assertThrows(PersistenceException.class, () -> productService.create(productRequestDto,multipartFiles),
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
        List<MultipartFile> multipartFiles = List.of(new MockMultipartFile(
            "file",
            "test.fdsf",
            "image/fdsf",
            new byte[0]
        ));
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

        var actual = productService.update(1L, productRequestDto, multipartFiles);

        verify(brandRepository).findById(eq(1L));
        verify(categoryRepository).findById(eq(1L));
        verify(productRepository).findByIdWithCollections(eq(1L));
        verify(attributeValueRepository).findAllByIdIn(eq(Collections.emptyList()));
        verify(productRepository).findByNameAndIdNot(eq(productRequestDto.getName()), eq(1L));
        verify(modelMapper).map(eq(product), eq(ProductResponseDto.class));
        assertEquals(productResponseDto, actual);
    }

    @Test
    void updateThrowsNotFoundExceptionWhenBrandIdNotExistsTest() {
        List<MultipartFile> multipartFiles = List.of(new MockMultipartFile(
            "file",
            "test.fdsf",
            "image/fdsf",
            new byte[0]
        ));
        var productRequestDto = getProductRequestDto();
        when(productRepository.findByIdWithCollections(1L))
            .thenReturn(Optional.of(getProduct()));
        when(brandRepository.findById(1L))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.update(1L, productRequestDto, multipartFiles),
            ExceptionMessage.BRAND_NOT_FOUND_BY_ID.formatted(1L));
        verify(brandRepository).findById(eq(1L));
        verify(productRepository).findByIdWithCollections(eq(1L));
    }

    @Test
    void updateThrowsNotFoundExceptionWhenCategoryIdNotExistsTest() {
        List<MultipartFile> multipartFiles = List.of(new MockMultipartFile(
            "file",
            "test.fdsf",
            "image/fdsf",
            new byte[0]
        ));
        var productRequestDto = getProductRequestDto();
        when(productRepository.findByIdWithCollections(1L))
            .thenReturn(Optional.of(getProduct()));
        when(brandRepository.findById(1L))
            .thenReturn(Optional.of(getBrand()));
        when(categoryRepository.findById(1L)).
            thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.update(1L, productRequestDto, multipartFiles),
            ExceptionMessage.CATEGORY_NOT_FOUND_BY_ID.formatted(1L));
        verify(brandRepository).findById(eq(1L));
        verify(categoryRepository).findById(eq(1L));
        verify(productRepository).findByIdWithCollections(eq(1L));
    }

    @Test
    void updateThrowsNotFoundExceptionWhenProductIdNotExistsTest() {
        List<MultipartFile> multipartFiles = List.of(new MockMultipartFile(
            "file",
            "test.fdsf",
            "image/fdsf",
            new byte[0]
        ));
        var product = getProduct();
        var productRequestDto = getProductRequestDto();
        when(productRepository.findByIdWithCollections(1L))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.update(1L, productRequestDto, multipartFiles),
            ExceptionMessage.PRODUCT_NOT_FOUND_BY_ID.formatted(product.getName()));
        verify(productRepository).findByIdWithCollections(eq(1L));
    }

    @Test
    void updateThrowsAlreadyExistsExceptionWhenProductNameExistsTest() {
        List<MultipartFile> multipartFiles = List.of(new MockMultipartFile(
            "file",
            "test.fdsf",
            "image/fdsf",
            new byte[0]
        ));
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

        assertThrows(AlreadyExistsException.class, () -> productService.update(1L, productRequestDto, multipartFiles),
            ExceptionMessage.PRODUCT_WITH_NAME_ALREADY_EXISTS.formatted(product.getName()));
        verify(brandRepository).findById(eq(1L));
        verify(categoryRepository).findById(eq(1L));
        verify(productRepository).findByNameAndIdNot(eq(product.getName()), eq(1L));
        verify(productRepository).findByIdWithCollections(eq(1L));
    }

    @Test
    void updateThrowsPersistenceExceptionWhenProductWithIncorrectAttributesTest() {
        List<MultipartFile> multipartFiles = List.of(new MockMultipartFile(
            "file",
            "test.fdsf",
            "image/fdsf",
            new byte[0]
        ));
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

        assertThrows(PersistenceException.class, () -> productService.update(1L, productRequestDto, multipartFiles),
            ExceptionMessage.PRODUCT_PERSISTENCE_EXCEPTION);
        verify(productRepository).findByIdWithCollections(eq(1L));
        verify(brandRepository).findById(eq(1L));
        verify(categoryRepository).findById(eq(1L));
        verify(productRepository).findByNameAndIdNot(eq(productRequestDto.getName()), eq(1L));
        verify(attributeValueRepository).getReferenceById(eq(3L));
        verify(attributeValueRepository).findAllByIdIn(eq(List.of(3L)));
    }
}