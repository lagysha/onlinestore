package io.teamchallenge.controller;

import io.teamchallenge.annotation.AllowedSortFields;
import io.teamchallenge.annotation.CurrentUserId;
import io.teamchallenge.annotation.ValidOrderRequest;
import io.teamchallenge.dto.order.OrderRequestDto;
import io.teamchallenge.dto.order.OrderResponseDto;
import io.teamchallenge.dto.order.OrderFilterDto;
import io.teamchallenge.dto.order.ShortOrderResponseDto;
import io.teamchallenge.dto.pageable.PageableDto;
import io.teamchallenge.enumerated.DeliveryStatus;
import io.teamchallenge.service.impl.OrderService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/v1/orders")
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
    //todo: test all endpoints
    //todo: create tests
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getById(orderId));
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<Void> setOrderStatus(@PathVariable Long orderId, @RequestParam DeliveryStatus status) {
        orderService.setDeliveryStatus(orderId, status);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId, @CurrentUserId Long userId) {
        orderService.cancelOrder(orderId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<PageableDto<ShortOrderResponseDto>> getAllOrders(
        @Valid OrderFilterDto filterParametersDto,
        @AllowedSortFields(values = {"id","createdAt","isPaid","deliveryStatus","deliveryMethod","total"})
        @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllByFilter(filterParametersDto, pageable));
    }
}