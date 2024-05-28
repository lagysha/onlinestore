package io.teamchallenge.mapper;

import io.teamchallenge.dto.product.ShortProductResponseDto;
import io.teamchallenge.entity.Image;

import static io.teamchallenge.util.Utils.getProduct;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShortProductResponseDtoMapperTest {

    @InjectMocks
    private ShortProductResponseDtoMapper shortProductResponseDtoMapper;

    @Test
    void convertTest() {
        var product = getProduct();

        var expected = ShortProductResponseDto
            .builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .images(product.getImages().stream()
                .map(Image::getLink)
                .collect(Collectors.toList()))
            .build();

        assertEquals(expected, shortProductResponseDtoMapper.convert(product));
    }
}
