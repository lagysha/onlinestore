package io.teamchallenge.controller;

import io.teamchallenge.dto.order.OrderRequestDto;
import io.teamchallenge.service.OrderService;
import io.teamchallenge.utils.Utils;
import java.security.Principal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

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

        ResponseEntity<Long> responseEntity = orderController.create(orderRequestDto, principal);

        assertEquals(CREATED, responseEntity.getStatusCode());
        assertEquals(expected, responseEntity.getBody());
        verify(orderService).create(orderRequestDto, principal);
    }
}
