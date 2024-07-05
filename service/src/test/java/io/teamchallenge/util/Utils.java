package io.teamchallenge.util;

import io.teamchallenge.dto.ImageDto;
import io.teamchallenge.dto.attributes.AttributeRequestDto;
import io.teamchallenge.dto.attributes.AttributeRequestUpdateDto;
import io.teamchallenge.dto.attributes.AttributeResponseDto;
import io.teamchallenge.dto.attributes.AttributeValuePatchRequestDto;
import io.teamchallenge.dto.attributes.AttributeValueResponseDto;
import io.teamchallenge.dto.brand.BrandRequestDto;
import io.teamchallenge.dto.brand.BrandResponseDto;
import io.teamchallenge.dto.category.CategoryAttributeAttributeValueVO;
import io.teamchallenge.dto.category.CategoryRequestDto;
import io.teamchallenge.dto.category.CategoryResponseDto;
import io.teamchallenge.dto.cart.CartItemResponseDto;
import io.teamchallenge.dto.cart.CartResponseDto;
import io.teamchallenge.dto.cart.CartItemPatchRequestDto;
import io.teamchallenge.dto.filter.PriceFilter;
import io.teamchallenge.dto.filter.ProductFilterDto;
import io.teamchallenge.dto.pageable.AdvancedPageableDto;
import io.teamchallenge.dto.product.ProductAttributeResponseDto;
import io.teamchallenge.dto.product.ProductMinMaxPriceDto;
import io.teamchallenge.dto.product.ProductRequestDto;
import io.teamchallenge.dto.product.ProductResponseDto;
import io.teamchallenge.dto.product.ShortProductResponseDto;
import io.teamchallenge.dto.security.SignInRequestDto;
import io.teamchallenge.dto.security.SignUpRequestDto;
import io.teamchallenge.dto.security.SignUpResponseDto;
import io.teamchallenge.entity.Brand;
import io.teamchallenge.entity.Category;
import io.teamchallenge.entity.Image;
import io.teamchallenge.entity.Product;
import io.teamchallenge.entity.User;
import io.teamchallenge.entity.attributes.Attribute;
import io.teamchallenge.entity.attributes.AttributeValue;
import io.teamchallenge.entity.attributes.ProductAttribute;
import io.teamchallenge.entity.cartitem.CartItem;
import io.teamchallenge.entity.cartitem.CartItemId;
import io.teamchallenge.enumerated.Role;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class Utils {

    public static final String PRODUCT_IMAGES_FOLDER_NAME = "productImages";
    public static final String SAMPLE_URL = "https://example.com";

    public static MultipartFile getMultipartFile(){
        return new MockMultipartFile(
            "file",
            "test.fdsf",
            "image/fdsf",
            new byte[0]
        );
    }
    public static Category getCategory() {
        return Category.builder()
            .id(1L)
            .name("name1")
            .description("Nothing")
            .build();
    }

    public static CartItem getCartItem() {
        return CartItem
            .builder()
            .id(new CartItemId(1L, 1L))
            .quantity(1)
            .user(getUser())
            .product(getProduct())
            .createdAt(LocalDateTime.now())
            .build();
    }

    public static ProductFilterDto getProductFilterDto(){
        return ProductFilterDto.builder()
            .name("Sample Product")
            .price(PriceFilter.builder()
                .from(1)
                .to(2)
                .build())
            .brandIds(List.of(1L))
            .categoryId(1L)
            .attributeValueIds(List.of(2L, 4L))
            .build();
    }

    public static CartItemPatchRequestDto getPatchRequestDto() {
        return CartItemPatchRequestDto
            .builder()
            .quantity(1)
            .build();
    }

    public static CartResponseDto getCartResponseDto() {
        return CartResponseDto
            .builder()
            .cartItemResponseDtos(List.of(getCartItemResponseDto()))
            .totalPrice(getCartItemResponseDto().getPrice())
            .build();
    }

    public static CartItemResponseDto getCartItemResponseDto() {
        return CartItemResponseDto
            .builder()
            .productId(1L)
            .quantity(1)
            .image("ddd")
            .name("name")
            .price(BigDecimal.ONE)
            .build();
    }

    public static Product getProduct() {
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

    public static Image getImage() {
        return Image.builder()
            .link("https://image.jpg").build();
    }

    public static ShortProductResponseDto getShortProductResponseDto() {
        var product = getProduct();
        return ShortProductResponseDto.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .images(product.getImages()
                .stream()
                .map(img -> ImageDto.builder()
                    .link(img.getLink())
                    .order(img.getOrder())
                    .build())
                .collect(Collectors.toList()))
            .build();
    }

    public static ProductResponseDto getProductResponseDto() {
        var product = getProduct();
        return ProductResponseDto.builder()
            .id(product.getId())
            .shortDesc(product.getShortDesc())
            .categoryResponseDto(
                CategoryResponseDto.builder()
                    .id(product.getCategory().getId())
                    .description(product.getCategory().getDescription())
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
                .map(img -> ImageDto.builder()
                    .link(img.getLink())
                    .order(img.getOrder())
                    .build())
                .collect(Collectors.toList()))
            .brand(product.getBrand().getName())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .quantity(product.getQuantity())
            .createdAt(product.getCreatedAt())
            .build();
    }

    public static ProductRequestDto getProductRequestDto() {
        return ProductRequestDto
            .builder()
            .shortDesc("shortDesc")
            .categoryId(1L)
            .attributeValueIds(List.of(1L))
            .brandId(1L)
            .name("name")
            .description("desc")
            .price(BigDecimal.ONE)
            .quantity(1)
            .build();
    }

    public static AttributeValue getAttributeValue() {
        return AttributeValue.builder()
            .id(1L)
            .attribute(getAttribute())
            .value("value")
            .build();
    }

    public static Attribute getAttribute() {
        return Attribute.builder().id(1L).name("name").build();
    }

    public static ProductAttribute getProductAttribute() {
        return ProductAttribute.builder()
            .attributeValue(getAttributeValue())
            .build();
    }

    public static Brand getBrand() {
        return Brand.builder()
            .id(1L)
            .name("name1")
            .build();
    }

    public static String getAccessToken() {
        return "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJpc3MiOiJHYWRnZXRIb3VzZSIsInN1YiI6ImV4YW1wbGUxMjNAZXhhbXBsZS5jb20iLCJpZCI6MTEsInJ" +
            "vbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MTY4MDA5NDcsImV4cCI6MTcxNzQwNTc0N30" +
            ".odua7l-MEZmfjsCu4jmAciqLI[lSRdvrD0Jmufd-N56";
    }

    public static String getRefreshToken() {
        return "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJpc3MiOiJHYWRnZXRIb3VzZSIsInN1YiI6ImV4YW1wbGUxMjNAZXhhbXBsZS5jb20iLCJpZCI6MTEsInJ" +
            "vbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MTY4MDA5NDcsImV4cCI6MTcxNzQwNTc0N30" +
            ".odua7l-MEZmfjsCu4jmAciqLI[lSRdvrD0Jmufd-N56";
    }

    public static User getUser() {
        return User.builder()
            .id(1L)
            .email("test@mail.com")
            .role(Role.ROLE_USER)
            .refreshTokenKey(getSecretKey())
            .createdAt(LocalDateTime.of(2024, 1, 1, 1, 1))
            .refreshTokenKey(getSecretKey())
            .createdAt(LocalDateTime.of(2024, 1, 1, 1, 1))
            .password("password")
            .phoneNumber("123456789010")
            .build();
    }

    public static User getNewUser() {
        User user = getUser();
        return User.builder()
            .email(user.getEmail())
            .password(user.getPassword())
            .phoneNumber(user.getPhoneNumber())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .role(user.getRole())
            .build();
    }

    public static SignUpRequestDto getSignUpRequestDto() {
        User newUser = getUser();
        return SignUpRequestDto.builder()
            .email(newUser.getEmail())
            .password("Password1234!")
            .phoneNumber(newUser.getPhoneNumber())
            .firstName(newUser.getFirstName())
            .lastName(newUser.getLastName())
            .build();
    }

    public static SignInRequestDto getSignInRequestDto() {
        User user = getUser();
        return SignInRequestDto.builder()
            .email(user.getEmail())
            .password("Password1234!")
            .build();
    }

    public static SignUpResponseDto getSignUpResponseDto() {
        User newUser = getUser();
        return SignUpResponseDto.builder()
            .id(newUser.getId())
            .email(newUser.getEmail())
            .firstName(newUser.getFirstName())
            .lastName(newUser.getLastName())
            .build();
    }

    public static String getSecretKey() {
        return "5cZAVF/SKSCmCM2+1azD2XHK7K2PChcSg32vrrEh/Qk=";
    }

    public static ProductMinMaxPriceDto getProductMinMaxPriceDto(){
        return new ProductMinMaxPriceDto(BigDecimal.ONE,BigDecimal.TWO);
    }

    public static AdvancedPageableDto<ShortProductResponseDto> getAdvancedPageableDto(){
        return AdvancedPageableDto.<ShortProductResponseDto>builder()
            .page(List.of(getShortProductResponseDto()))
            .totalElements(1)
            .currentPage(0)
            .totalPages(1)
            .minPrice(getProductMinMaxPriceDto().getMin())
            .maxPrice(getProductMinMaxPriceDto().getMax())
            .build();
    }

    public static CategoryAttributeAttributeValueVO getAttributeAttributeValueVO(){
        return CategoryAttributeAttributeValueVO.builder()
            .attributeId(1L)
            .attributeName("Size")
            .attributeValueId(1L)
            .attributeValueName("Big")
            .build();
    }

    public static CategoryResponseDto getCategoryResponseDto(){
        var category = getCategory();
        return CategoryResponseDto
            .builder()
            .id(category.getId())
            .name(category.getName())
            .description(category.getDescription())
            .build();
    }

    public static AttributeRequestDto getAttributeRequestDto(){
        return AttributeRequestDto.builder()
            .categoryId(1L)
            .name("Color")
            .build();
    }

    public static AttributeResponseDto getAttributeResponseDto(){
        return AttributeResponseDto.builder()
            .id(1L)
            .name("Color")
            .build();
    }

    public static BrandRequestDto getBrandRequestDto(){
        return BrandRequestDto.builder()
            .name("Apple")
            .build();
    }

    public static BrandResponseDto getBrandResponseDto(){
        return BrandResponseDto.builder()
            .id(1L)
            .name("Apple")
            .build();
    }

    public static CategoryRequestDto getCategoryRequestDto(){
        return CategoryRequestDto.builder()
            .name("name1")
            .description("Nothing")
            .build();
    }

    public static AttributeRequestUpdateDto getAttributeRequestUpdateDto() {
        return AttributeRequestUpdateDto.builder()
            .name("Color")
            .build();
    }

    public static AttributeValuePatchRequestDto getAttributeValuePatchRequestDto() {
        return AttributeValuePatchRequestDto.builder()
            .value("1")
            .build();
    }

    public static AttributeValueResponseDto getAttributeValueResponseDto() {
        return AttributeValueResponseDto.builder()
            .id(1L)
            .value("1")
            .build();
    }
}
