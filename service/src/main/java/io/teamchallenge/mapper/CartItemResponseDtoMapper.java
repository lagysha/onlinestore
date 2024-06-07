package io.teamchallenge.mapper;

import io.teamchallenge.dto.cart.CartItemResponseDto;
import io.teamchallenge.entity.Image;
import io.teamchallenge.entity.cartitem.CartItem;
import java.util.stream.Collectors;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

/**
 * Mapper for {@link CartItem}.
 * @author Niktia Malov
 */
public class CartItemResponseDtoMapper extends AbstractConverter<CartItem, CartItemResponseDto> {
    /**
     * Converts a CartItem entity into a CartItemResponseDto object.
     *
     * @param cartItem The CartItem entity to convert.
     * @return The converted CartItemResponseDto object.
     */
    @Override
    protected CartItemResponseDto convert(CartItem cartItem) {
        return CartItemResponseDto.builder()
            .productId(cartItem.getProduct().getId())
            .name(cartItem.getProduct().getName())
            .price(cartItem.getProduct().getPrice())
            .images(cartItem.getProduct().getImages()
                .stream()
                .map(Image::getLink)
                .collect(Collectors.toList()))
            .quantity(cartItem.getQuantity())
            .build();
    }
}
