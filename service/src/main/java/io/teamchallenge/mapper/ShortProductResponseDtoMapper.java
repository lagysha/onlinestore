package io.teamchallenge.mapper;


import io.teamchallenge.dto.ShortProductResponseDto;
import io.teamchallenge.entity.Image;
import io.teamchallenge.entity.Product;
import java.util.stream.Collectors;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class ShortProductResponseDtoMapper extends AbstractConverter<Product, ShortProductResponseDto> {
    @Override
    protected ShortProductResponseDto convert(Product product) {
        return ShortProductResponseDto.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .images(product.getImages().stream()
                .map(Image::getLink)
                .collect(Collectors.toList()))
            .build();
    }
}
