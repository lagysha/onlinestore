package io.teamchallenge.mapper;

import io.teamchallenge.dto.ImageDto;
import io.teamchallenge.dto.product.ShortProductResponseDto;
import io.teamchallenge.entity.Product;
import io.teamchallenge.entity.reviews.Review;
import java.util.stream.Collectors;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

/**
 * Mapper for {@link Product}.
 *
 * @author Niktia Malov
 */
@Component
public class ShortProductResponseDtoMapper extends AbstractConverter<Product, ShortProductResponseDto> {
    /**
     * Converts a Product entity to a ShortProductResponseDto.
     *
     * @param product The Product entity to be converted.
     * @return A ShortProductResponseDto representing the converted product.
     */
    @Override
    protected ShortProductResponseDto convert(Product product) {
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
            .available(product.getQuantity() > 0)
            .rating(product.getReviews().stream()
                .mapToInt(Review::getRate)
                .average()
                .orElse(0))
            .build();
    }
}
