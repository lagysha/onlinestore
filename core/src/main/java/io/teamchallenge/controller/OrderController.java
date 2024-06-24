package io.teamchallenge.controller;

import io.teamchallenge.annotation.ValidOrderRequest;
import io.teamchallenge.dto.order.OrderRequestDto;
import io.teamchallenge.service.OrderService;
import java.security.Principal;
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

    /**
     * Creates a new order based on the provided {@link OrderRequestDto} and the authenticated user.
     * This method handles HTTP POST requests to create a new order. It uses the {@link OrderRequestDto} to gather
     * order details and the {@link Principal} to identify the authenticated user.
     *
     * @param orderRequestDto the DTO containing the details of the order request.
     * @param userPrincipal the authenticated user's principal.
     * @return a {@link ResponseEntity} containing the ID of the newly created order and  HTTP status {@code CREATED}.
     */
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @ValidOrderRequest OrderRequestDto orderRequestDto,
                                       Principal userPrincipal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(orderRequestDto, userPrincipal));
    }
}