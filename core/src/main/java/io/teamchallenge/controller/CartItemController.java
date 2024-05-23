package io.teamchallenge.controller;

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

    //TODO : I need an annotation which will give me the current user
    @GetMapping("/{user_id}")
    public ResponseEntity<CartResponseDto> getByUserId(@PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(cartItemService.getByUserId(userId));
    }

    //TODO : I need an annotation which will give me the current user
    @PostMapping("/{user_id}/{product_id}")
    public ResponseEntity<CartItemResponseDto> create(@PathVariable("user_id") Long userId,
                                                      @PathVariable("product_id") Long productId) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(cartItemService.create(userId, productId));
    }

    //TODO : I need an annotation which will give me the current user
    @DeleteMapping("/{user_id}/{product_id}")
    public ResponseEntity<CartItemResponseDto> delete(@PathVariable("user_id") Long userId,
                                                      @PathVariable("product_id") Long productId) {
        cartItemService.deleteByCartItemId(userId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //TODO : I need an annotation which will give me the current user
    @PatchMapping("/{user_id}/{product_id}")
    public ResponseEntity<CartItemResponseDto> patchUpdate(@PathVariable("user_id") Long userId,
                                                           @PathVariable("product_id") Long productId,
                                                           @RequestBody @Valid PathRequestDto pathRequestDto) {
        return ResponseEntity.ok(cartItemService.patch(userId, productId, pathRequestDto));
    }
}
