package io.teamchallenge.controller;

import io.teamchallenge.service.CartItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.teamchallenge.utils.Utils.getCartItemResponseDto;
import static io.teamchallenge.utils.Utils.getCartResponseDto;
import static io.teamchallenge.utils.Utils.getPatchRequestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class CartItemControllerTest {

    @InjectMocks
    private CartItemController cartItemController;

    @Mock
    private CartItemService cartItemService;

    @Test
    void getByUserIdTest() {
        Long userId = 1L;
        var response = getCartResponseDto();
        when(cartItemService.getByUserId(userId)).thenReturn(response);

        var responseEntity = cartItemController.getByUserId(userId);

        verify(cartItemService).getByUserId(eq(userId));
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }

    @Test
    void createTest() {
        Long userId = 1L;
        Long productId = 1L;
        var response = getCartItemResponseDto();
        when(cartItemService.create(userId, productId)).thenReturn(response);

        var responseEntity = cartItemController.create(userId, productId);

        verify(cartItemService).create(eq(userId), eq(productId));
        assertEquals(CREATED, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }

    @Test
    void deleteTest() {
        Long userId = 1L;
        Long productId = 1L;
        doNothing().when(cartItemService).deleteByCartItemId(userId, productId);

        var responseEntity = cartItemController.delete(userId, productId);

        verify(cartItemService).deleteByCartItemId(eq(userId), eq(productId));
        assertEquals(NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void patchUpdateTest() {
        Long userId = 1L;
        Long productId = 1L;
        var request = getPatchRequestDto();
        var response = getCartItemResponseDto();
        when(cartItemService.patch(userId, productId, request)).thenReturn(response);

        var responseEntity = cartItemController.patchUpdate(userId, productId, request);

        verify(cartItemService).patch(eq(userId), eq(productId), eq(request));
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }
}
