package io.teamchallenge.controller;

import io.teamchallenge.dto.PageableDto;
import io.teamchallenge.service.ProductService;
import io.teamchallenge.utils.Utils;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Test
    void getAllTest() {
        var pageable = PageRequest.of(1, 1, Sort.by("price"));
        var name = "phone";
        var response = new PageableDto<>(
            List.of(Utils.getShortProductResponseDto()), 1, 1, 1);
        when(productService.getAll(pageable, name)).thenReturn(response);

        var responseEntity = productController.getAll(name,pageable);

        verify(productService).getAll(eq(pageable),eq(name));
        assertEquals(OK,responseEntity.getStatusCode());
        assertEquals(response,responseEntity.getBody());
    }

    @Test
    void getByIdTest() {
        var id = 1L;
        var response = Utils.getProductResponseDto();
        when(productService.getById(id)).thenReturn(response);

        var responseEntity = productController.getById(id);

        verify(productService).getById(eq(id));
        assertEquals(OK,responseEntity.getStatusCode());
        assertEquals(response,responseEntity.getBody());
    }

    @Test
    void createTest() {
        var request = Utils.getProductRequestDto();
        var response = Utils.getProductResponseDto();
        when(productService.create(request)).thenReturn(response);

        var responseEntity = productController.create(request);

        verify(productService).create(eq(request));
        assertEquals(CREATED,responseEntity.getStatusCode());
        assertEquals(response,responseEntity.getBody());
    }

    @Test
    void updateTest() {
        var id = 1L;
        var request = Utils.getProductRequestDto();
        var response = Utils.getProductResponseDto();
        when(productService.update(id,request)).thenReturn(response);

        var responseEntity = productController.update(id,request);

        verify(productService).update(eq(id),eq(request));
        assertEquals(OK,responseEntity.getStatusCode());
        assertEquals(response,responseEntity.getBody());
    }

    @Test
    void deleteTest() {
        var id = 1L;
        doNothing().when(productService).deleteById(id);

        var responseEntity = productController.delete(id);

        verify(productService).deleteById(eq(id));
        assertEquals(NO_CONTENT,responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
}
