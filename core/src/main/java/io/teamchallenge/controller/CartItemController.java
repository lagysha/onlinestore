package io.teamchallenge.controller;

import io.teamchallenge.annatation.AllowedSortFields;
import io.teamchallenge.dto.CartItemResponseDto;
import io.teamchallenge.dto.PageableDto;
import io.teamchallenge.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import static org.springframework.data.domain.Sort.Direction.DESC;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart-items")
public class CartItemController {
    private final CartItemService cartItemService;

    @GetMapping("/{user_id}")
    public ResponseEntity<PageableDto<CartItemResponseDto>> getByUserId(@PathVariable("user_id") Long userId,
                                                                        @AllowedSortFields(values = {"quantity",
                                                                            "createdAt"})
                                                                        @PageableDefault(sort = "createdAt", direction = DESC)
                                                                        Pageable pageable) {
        return ResponseEntity.ok(cartItemService.getByUserId(userId, pageable));
    }

}
