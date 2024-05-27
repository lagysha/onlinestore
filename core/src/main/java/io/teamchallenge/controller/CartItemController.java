package io.teamchallenge.controller;

import io.teamchallenge.annotation.CurrentUserId;
import io.teamchallenge.dto.cart.CartItemResponseDto;
import io.teamchallenge.dto.cart.CartResponseDto;
import io.teamchallenge.dto.cart.PathRequestDto;
import io.teamchallenge.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart-items")
public class CartItemController {
    private final CartItemService cartItemService;

    /**
     * Retrieves the cart items for a specific user.
     *
     * @param userId the ID of the current user obtained from the security context
     * @return a ResponseEntity containing a CartResponseDto with the cart items of the user
     */
    @GetMapping
    public ResponseEntity<CartResponseDto> getByUserId(@CurrentUserId Long userId) {
        return ResponseEntity.ok(cartItemService.getByUserId(userId));
    }

    /**
     * Adds a product to the cart for a specific user.
     *
     * @param userId    the ID of the current user obtained from the security context
     * @param productId the ID of the product to be added to the cart
     * @return a ResponseEntity containing a CartItemResponseDto with the added cart item
     */
    @PostMapping("/{product_id}")
    public ResponseEntity<CartItemResponseDto> create(@CurrentUserId Long userId,
                                                      @PathVariable("product_id") Long productId) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(cartItemService.create(userId, productId));
    }

    /**
     * Removes a product from the cart for a specific user.
     *
     * @param userId    the ID of the current user obtained from the security context
     * @param productId the ID of the product to be removed from the cart
     * @return a ResponseEntity indicating the status of the operation
     */
    @DeleteMapping("/{product_id}")
    public ResponseEntity<Void> delete(@CurrentUserId Long userId,
                                       @PathVariable("product_id") Long productId) {
        cartItemService.deleteByCartItemId(userId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Updates a cart item partially for a specific user.
     *
     * @param userId          the ID of the current user obtained from the security context
     * @param productId       the ID of the product to be updated in the cart
     * @param pathRequestDto  the request body containing the fields to be updated
     * @return a ResponseEntity containing a CartItemResponseDto with the updated cart item
     */
    @PatchMapping("/{product_id}")
    public ResponseEntity<CartItemResponseDto> patchUpdate(@CurrentUserId Long userId,
                                                           @PathVariable("product_id") Long productId,
                                                           @RequestBody @Valid PathRequestDto pathRequestDto) {
        return ResponseEntity.ok(cartItemService.patch(userId, productId, pathRequestDto));
    }
}
