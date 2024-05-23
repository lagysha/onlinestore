package io.teamchallenge.util;

import io.teamchallenge.dto.CategoryResponseDto;
import io.teamchallenge.dto.product.ProductAttributeResponseDto;
import io.teamchallenge.dto.product.ProductRequestDto;
import io.teamchallenge.dto.product.ProductResponseDto;
import io.teamchallenge.dto.product.ShortProductResponseDto;
import io.teamchallenge.dto.user.UserVO;
import io.teamchallenge.entity.Brand;
import io.teamchallenge.entity.Category;
import io.teamchallenge.entity.Image;
import io.teamchallenge.entity.Product;
import io.teamchallenge.entity.attributes.Attribute;
import io.teamchallenge.entity.attributes.AttributeValue;
import io.teamchallenge.entity.attributes.ProductAttribute;
import io.teamchallenge.enumerated.Role;
import java.math.BigDecimal;
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
            .images(product.getImages().stream()
                .map(Image::getLink)
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

    public static ProductRequestDto getProductRequestDto() {
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

    public static UserVO getUserVO() {
        return UserVO.builder()
            .id(1L)
            .email("testmail@gmail.com")
            .role(Role.ROLE_USER)
            .build();
    }
}
