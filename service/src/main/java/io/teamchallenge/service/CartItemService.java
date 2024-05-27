package io.teamchallenge.service;

import io.teamchallenge.constant.ExceptionMessage;
import io.teamchallenge.dto.cart.CartItemResponseDto;
import io.teamchallenge.dto.cart.CartResponseDto;
import io.teamchallenge.dto.cart.PatchRequestDto;
import io.teamchallenge.entity.cartitem.CartItem;
import io.teamchallenge.entity.cartitem.CartItemId;
import io.teamchallenge.exception.AlreadyExistsException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.CartItemRepository;
import io.teamchallenge.repository.ProductRepository;
import io.teamchallenge.repository.UserRepository;
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

import static io.teamchallenge.constant.AppConstant.MAX_NUMBER_OF_UNIQUE_PRODUCTS_IN_CART;
import static io.teamchallenge.constant.ExceptionMessage.CARTITEM_ALREADY_EXISTS;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * Retrieves the cart items for a specific user.
     *
     * @param userId the ID of the user whose cart items are to be retrieved
     * @return a CartResponseDto containing the cart items and total price of the cart
     */
    public CartResponseDto getByUserId(Long userId) {
        Pageable pageable = PageRequest.of(0, MAX_NUMBER_OF_UNIQUE_PRODUCTS_IN_CART,
            Sort.by("createdAt"));

        Page<CartItemId> retrievedCartItemsIds = cartItemRepository.findCartItemIdsByUserId(userId, pageable);

        return cartItemRepository
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

    /**
     * Creates a new cart item for a specific user and product.
     *
     * @param userId    the ID of the user for whom the cart item is to be created
     * @param productId the ID of the product to be added to the cart
     * @return a CartItemResponseDto containing the created cart item details
     */
    @Transactional
    public CartItemResponseDto create(Long userId, Long productId) {
        CartItemId cartItemId = CartItemId.builder()
            .productId(productId)
            .userId(userId).build();

        validateCartItemId(cartItemId);
        var product = productRepository.findByIdWithImages(productId)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND_BY_ID.formatted(productId)));
        var user = userRepository.getReferenceById(userId);

        var cartItem = CartItem.builder()
            .id(cartItemId)
            .product(product)
            .user(user)
            .quantity(1)
            .build();
        var savedCartItem = cartItemRepository.persist(cartItem);

        return modelMapper.map(savedCartItem, CartItemResponseDto.class);
    }

    /**
     * Deletes a cart item by user ID and product ID.
     *
     * @param userId    the ID of the user who owns the cart item
     * @param productId the ID of the product to be removed from the cart
     */
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

    /**
     * Partially updates the quantity of a cart item for a specific user and product.
     *
     * @param userId         the ID of the user who owns the cart item
     * @param productId      the ID of the product in the cart item
     * @param patchRequestDto the request body containing the fields to be updated
     * @return a CartItemResponseDto containing the updated cart item details
     */
    @Transactional
    public CartItemResponseDto patch(Long userId, Long productId, PatchRequestDto patchRequestDto) {
        CartItemId cartItemId = CartItemId.builder()
            .productId(productId)
            .userId(userId).build();

        var retrievedCartItem = cartItemRepository
            .findById(cartItemId)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.CARTITEM_NOT_FOUND_BY_ID.formatted(cartItemId)));

        retrievedCartItem.setQuantity(patchRequestDto.getQuantity());

        return modelMapper.map(retrievedCartItem, CartItemResponseDto.class);
    }

    private void validateCartItemId(CartItemId cartItemId) {
        var retrievedCartItem = cartItemRepository.findById(cartItemId);
        if (retrievedCartItem.isPresent()) {
            throw new AlreadyExistsException(CARTITEM_ALREADY_EXISTS.formatted(retrievedCartItem.get().getId()));
        }
    }
}
