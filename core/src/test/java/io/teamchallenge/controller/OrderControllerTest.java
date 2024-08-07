package io.teamchallenge.controller;

import io.teamchallenge.dto.order.OrderFilterDto;
import io.teamchallenge.dto.order.OrderRequestDto;
import io.teamchallenge.dto.order.OrderResponseDto;
import io.teamchallenge.dto.order.ShortOrderResponseDto;
import io.teamchallenge.dto.pageable.PageableDto;
import io.teamchallenge.enumerated.DeliveryStatus;
import io.teamchallenge.service.impl.OrderService;
import io.teamchallenge.utils.Utils;
import java.security.Principal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import static io.teamchallenge.utils.Utils.getOrderResponseDto;
import static io.teamchallenge.utils.Utils.getShortOrderResponseDtoPageableDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Test
    void createTest() {
        OrderRequestDto orderRequestDto = Utils.getOrderRequestDtoCourier();
        Principal principal = () -> "test@mail.com";
        Long expected = 1234L;
        when(orderService.create(orderRequestDto, principal)).thenReturn(expected);

        var responseEntity = orderController.create(orderRequestDto, principal);

        assertEquals(CREATED, responseEntity.getStatusCode());
        assertEquals(expected, responseEntity.getBody());
        verify(orderService).create(orderRequestDto, principal);
    }

    @Test
    void getOrderByIdTest() {
        Long orderId = 1L;
        OrderResponseDto orderResponseDto = getOrderResponseDto();
        when(orderService.getById(orderId)).thenReturn(orderResponseDto);

        var responseEntity = orderController.getOrderById(orderId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(orderResponseDto, responseEntity.getBody());
        verify(orderService).getById(orderId);
    }

    @Test
    void setOrderStatusTest() {
        Long orderId = 1L;
        DeliveryStatus status = DeliveryStatus.COMPLETED;

        var responseEntity = orderController.setOrderStatus(orderId, status);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(orderService).setDeliveryStatus(orderId, status);
    }

    @Test
    void cancelOrderTest() {
        Long orderId = 1L;
        Long userId = 1L;

        var responseEntity = orderController.cancelOrder(orderId, userId);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(orderService).cancelOrder(orderId, userId);
    }

    @Test
    void getAllOrdersTest() {
        OrderFilterDto filterParametersDto = new OrderFilterDto();
        Pageable pageable = PageRequest.of(0, 10);
        PageableDto<ShortOrderResponseDto> pageableDto = getShortOrderResponseDtoPageableDto();
        when(orderService.getAllByFilter(filterParametersDto, pageable)).thenReturn(pageableDto);

        var responseEntity = orderController.getAllOrders(filterParametersDto, pageable);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(pageableDto, responseEntity.getBody());
        verify(orderService).getAllByFilter(filterParametersDto, pageable);
    }
}
