package io.teamchallenge.mapper;

import io.teamchallenge.dto.ImageDto;
import io.teamchallenge.dto.product.ShortProductResponseDto;
import io.teamchallenge.entity.Image;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.teamchallenge.util.Utils.getProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
            .images(product.getImages()
                .stream()
                .map(img -> ImageDto.builder()
                    .link(img.getLink())
                    .order(img.getOrder())
                    .build())
                .collect(Collectors.toList()))
            .build();

        assertEquals(expected, shortProductResponseDtoMapper.convert(product));
    }
}
