package io.teamchallenge.controller;

import io.teamchallenge.annotation.CurrentUserId;
import io.teamchallenge.dto.order.OrderRequestDto;
import io.teamchallenge.dto.order.OrderResponseDto;
import io.teamchallenge.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> create(@RequestBody OrderRequestDto orderRequestDto,
                                                   @CurrentUserId Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(orderRequestDto, userId));
    }
}
