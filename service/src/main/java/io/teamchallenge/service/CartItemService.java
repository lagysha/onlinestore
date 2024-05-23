package io.teamchallenge.service;

import static io.teamchallenge.constant.AppConstant.MAX_NUMBER_OF_UNIQUE_PRODUCTS_IN_CART;

import io.teamchallenge.constant.ExceptionMessage;

import static io.teamchallenge.constant.ExceptionMessage.CARTITEM_ALREADY_EXISTS;

import io.teamchallenge.dto.cart.CartItemResponseDto;
import io.teamchallenge.dto.cart.CartResponseDto;
import io.teamchallenge.dto.cart.PathRequestDto;
import io.teamchallenge.entity.cartitem.CartItem;
import io.teamchallenge.entity.cartitem.CartItemId;
import io.teamchallenge.exception.AlreadyExistsException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.CartItemRepository;
import io.teamchallenge.repository.ProductRepository;
import io.teamchallenge.repository.UserRepository;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collector;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public CartResponseDto getByUserId(Long userId) {
        Pageable pageable = PageRequest.of(0, MAX_NUMBER_OF_UNIQUE_PRODUCTS_IN_CART, Sort.by("createdAt"));

        Page<CartItemId> retrievedCartItemsIds = cartItemRepository.findCartItemIdsByUserId(userId, pageable);

        return cartItemRepository
            //TODO : implement order of images in the database with save
            //TODO : here fetch only first image
            .findAllByIdWithImagesAndProducts(retrievedCartItemsIds.getContent())
            .stream()
            .collect(
                Collector.of(
                    () -> new CartResponseDto(new ArrayList<>(), BigDecimal.ZERO),
                    (acc, cartItem) -> {
                        acc.setTotalPrice(acc.getTotalPrice().add(
                            cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))));
                        acc.getCartItemResponseDtos().add(modelMapper.map(cartItem, CartItemResponseDto.class));
                    },
                    (acc1, acc2) -> {
                        acc1.setTotalPrice(acc1.getTotalPrice().add(acc2.getTotalPrice()));
                        acc1.getCartItemResponseDtos().addAll(acc2.getCartItemResponseDtos());
                        return acc1;
                    }
                )
            );
    }

    @Transactional
    public CartItemResponseDto create(Long userId, Long productId) {
        CartItemId cartItemId = CartItemId.builder()
            .productId(productId)
            .userId(userId).build();

        validateCartItemId(cartItemId);
        var product = productRepository.findByIdWithImages(productId)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND_BY_ID.formatted(productId)));

        //TODO : delete this when I will have an annotation to retrieve current user
        var user = userRepository.findById(userId)
            .orElseThrow();

        var cartItem = CartItem.builder()
            .id(cartItemId)
            .product(product)
            .user(user)
            .quantity(1)
            .build();
        var savedCartItem = cartItemRepository.persist(cartItem);

        return modelMapper.map(savedCartItem, CartItemResponseDto.class);
    }

    @Transactional
    public void deleteByCartItemId(Long userId, Long productId) {
        CartItemId cartItemId = CartItemId.builder()
            .productId(productId)
            .userId(userId).build();

        cartItemRepository
            .findById(cartItemId)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.CARTITEM_NOT_FOUND_BY_ID.formatted(cartItemId)));

        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    public CartItemResponseDto patch(Long userId, Long productId, PathRequestDto pathRequestDto) {
        CartItemId cartItemId = CartItemId.builder()
            .productId(productId)
            .userId(userId).build();

        var retrievedCartItem = cartItemRepository
            .findById(cartItemId)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.CARTITEM_NOT_FOUND_BY_ID.formatted(cartItemId)));

        retrievedCartItem.setQuantity(pathRequestDto.getQuantity());

        return modelMapper.map(retrievedCartItem, CartItemResponseDto.class);
    }

    private void validateCartItemId(CartItemId cartItemId) {
        var retrievedCartItem = cartItemRepository.findById(
            cartItemId
        );
        if (retrievedCartItem.isPresent()) {
            throw new AlreadyExistsException(
                CARTITEM_ALREADY_EXISTS.formatted(retrievedCartItem.get().getId()));
        }
    }

}
