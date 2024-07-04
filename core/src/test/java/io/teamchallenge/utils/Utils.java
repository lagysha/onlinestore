package io.teamchallenge.utils;

import io.teamchallenge.dto.ImageDto;
import io.teamchallenge.dto.address.AddressDto;
import io.teamchallenge.dto.attributes.AttributeAttributeValueDto;
import io.teamchallenge.dto.attributes.AttributeValueDto;
import io.teamchallenge.dto.cart.CartItemResponseDto;
import io.teamchallenge.dto.cart.CartResponseDto;
import io.teamchallenge.dto.cart.PatchRequestDto;
import io.teamchallenge.dto.category.CategoryResponseDto;
import io.teamchallenge.dto.filter.PriceFilter;
import io.teamchallenge.dto.filter.ProductFilterDto;
import io.teamchallenge.dto.order.OrderRequestDto;
import io.teamchallenge.dto.pageable.AdvancedPageableDto;
import io.teamchallenge.dto.product.ProductAttributeResponseDto;
import io.teamchallenge.dto.product.ProductRequestDto;
import io.teamchallenge.dto.product.ProductResponseDto;
import io.teamchallenge.dto.product.ShortProductResponseDto;
import io.teamchallenge.dto.review.AddReviewRequestDto;
import io.teamchallenge.dto.review.ReviewResponseDto;
import io.teamchallenge.dto.security.SignInRequestDto;
import io.teamchallenge.dto.security.SignInResponseDto;
import io.teamchallenge.dto.security.SignUpRequestDto;
import io.teamchallenge.dto.security.SignUpResponseDto;
import io.teamchallenge.dto.user.ReviewerDto;
import io.teamchallenge.entity.Brand;
import io.teamchallenge.entity.Category;
import io.teamchallenge.entity.Image;
import io.teamchallenge.entity.Product;
import io.teamchallenge.entity.attributes.Attribute;
import io.teamchallenge.entity.attributes.AttributeValue;
import io.teamchallenge.entity.attributes.ProductAttribute;
import io.teamchallenge.entity.reviews.ReviewId;
import io.teamchallenge.enumerated.DeliveryMethod;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static Category getCategory() {
        return Category.builder()
            .id(1L)
            .name("name1")
            .build();
    }

    public static PatchRequestDto getPatchRequestDto() {
        return PatchRequestDto
            .builder()
            .quantity(1)
            .build();
    }

    public static CartResponseDto getCartResponseDto() {
        return CartResponseDto
            .builder()
            .cartItemResponseDtos(new ArrayList<>())
            .totalPrice(BigDecimal.ZERO)
            .build();
    }

    public static CartItemResponseDto getCartItemResponseDto() {
        return CartItemResponseDto
            .builder()
            .productId(1L)
            .quantity(1)
            .image("dddd")
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
                    .id(product.getId())
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
            .attributeValueId(List.of(1L))
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

    public static SignInResponseDto getSignInResponseDto() {
        return SignInResponseDto.builder()
            .accessToken("accessToken")
            .refreshToken("refreshToken")
            .build();
    }

    public static SignUpRequestDto getSignUpRequestDto() {
        return SignUpRequestDto.builder()
            .email("test@mail.com")
            .firstName("John")
            .lastName("Doe")
            .phoneNumber("0123456789")
            .password("Password1234!")
            .build();
    }

    public static SignUpResponseDto getSignUpResponseDto() {
        return SignUpResponseDto.builder()
            .id(1L)
            .email("test@mail.com")
            .firstName("John")
            .lastName("Doe")
            .build();
    }

    public static SignInRequestDto getSignInRequestDto() {
        return SignInRequestDto.builder()
            .email("test@mail.com")
            .password("Password1234!")
            .build();
    }


    public static String getAccessToken() {
        return "eyJhbGciOiJIUzI1NiJ9" +
               ".eyJpc3MiOiJHYWRnZXRIb3VzZSIsInN1YiI6ImV4YW1wbGUxMjNAZXhhbXBsZS5jb20iLCJpZCI6MTEsInJ" +
               "vbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MTY4MDA5NDcsImV4cCI6MTcxNzQwNTc0N30" +
               ".odua7l-MEZmfjsCu4jmAciqLI[lSRdvrD0Jmufd-N56";
    }

    public static String getSecretKey() {
        return "5cZAVF/SKSCmCM2+1azD2XHK7K2PChcSg32vrrEh/Qk=";
    }

    public static AdvancedPageableDto<ShortProductResponseDto> getAdvancedPageableDto() {
        return AdvancedPageableDto.<ShortProductResponseDto>builder()
            .page(List.of(getShortProductResponseDto()))
            .totalElements(1)
            .currentPage(1)
            .totalPages(1)
            .minPrice(BigDecimal.valueOf(2))
            .maxPrice(BigDecimal.valueOf(3))
            .build();
    }

    public static ProductFilterDto getProductFilterDto() {
        return ProductFilterDto.builder()
            .name("Sample Product")
            .price(PriceFilter.builder()
                .from(100)
                .to(500)
                .build())
            .brandIds(List.of(1L))
            .categoryId(1L)
            .attributeValueIds(List.of(2L, 4L))
            .build();
    }

    public static AttributeAttributeValueDto getAttributeAttributeValueDto() {
        return AttributeAttributeValueDto.builder()
            .id(1L)
            .name("Size")
            .attributeValueDtos(
                List.of((AttributeValueDto
                    .builder()
                    .id(1L)
                    .name("Big")
                    .build())
                ))
            .build();
    }

    public static CategoryResponseDto getCategoryResponseDto() {
        return CategoryResponseDto.builder()
            .id(1L)
            .name("Laptops")
            .description("Nothing")
            .build();
    }



    public static OrderRequestDto getOrderRequestDtoCourier() {
        return OrderRequestDto.builder()
            .firstName("FirstName")
            .lastName("LastName")
            .address(AddressDto.builder()
                .addressLine("address line")
                .city("Kyiv")
                .postalCode("12322")
                .countryName("Ukraine")
                .build())
            .deliveryMethod(DeliveryMethod.COURIER)
            .phoneNumber("1234567890")
            .email("test@mail.com")
            .build();
    }

    public static String getRefreshToken() {
        return "eyJhbGciOiJIUzI1NiJ9" +
               ".eyJpc3MiOiJHYWRnZXRIb3VzZSIsInN1YiI6ImV4YW1wbGUxMjNAZXhhbXBsZS5jb20iLCJpZCI6MTEsInJ" +
               "vbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MTY4MDA5NDcsImV4cCI6MTcxNzQwNTc0N30" +
               ".odua7l-MEZmfjsCu4jmAciqLI[lSRdvrD0Jmufd-N56";
    }

    public static ReviewResponseDto getReviewResponseDto() {
        return ReviewResponseDto.builder()
            .text("test text")
            .rate((short) 4)
            .createdAt(LocalDateTime.of(1,1,1,1,1))
            .user(ReviewerDto.builder()
                .firstName("test name")
                .lastName("test surname")
                .build())
            .build();
    }

    public static ReviewId getReviewId() {
        return ReviewId.builder()
            .userId(1L)
            .productId(1L)
            .build();
    }

    public static AddReviewRequestDto getAddReviewRequestDto() {
        return AddReviewRequestDto.builder()
            .text("test text")
            .rate((short) 4)
            .build();
    }
}
