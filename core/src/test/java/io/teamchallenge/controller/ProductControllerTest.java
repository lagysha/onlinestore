package io.teamchallenge.controller;

import io.teamchallenge.service.impl.ProductService;
import io.teamchallenge.utils.Utils;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static io.teamchallenge.utils.Utils.getAdvancedPageableDto;
import static io.teamchallenge.utils.Utils.getProductFilterDto;
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
        var response = getAdvancedPageableDto();
        var filter = getProductFilterDto();
        when(productService.getAll(pageable, filter)).thenReturn(response);

        var responseEntity = productController.getAll(filter, pageable);

        verify(productService).getAll(eq(pageable), eq(filter));
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }

    @Test
    void getByIdTest() {
        var id = 1L;
        var response = Utils.getProductResponseDto();
        when(productService.getById(id)).thenReturn(response);

        var responseEntity = productController.getById(id);

        verify(productService).getById(eq(id));
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }

    @Test
    void createTest() {
        var request = Utils.getProductRequestDto();
        var response = Utils.getProductResponseDto();
        List<MultipartFile> multipartFiles = List.of(new MockMultipartFile(
            "file",
            "test.fdsf",
            "image/fdsf",
            new byte[0]
        ));
        when(productService.create(request,multipartFiles)).thenReturn(response);

        var responseEntity = productController.create(multipartFiles,request);

        verify(productService).create(eq(request),eq(multipartFiles));
        assertEquals(CREATED, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }

    @Test
    void updateTest() {
        var id = 1L;
        List<MultipartFile> multipartFiles = List.of(new MockMultipartFile(
            "file",
            "test.fdsf",
            "image/fdsf",
            new byte[0]
        ));
        var request = Utils.getProductRequestDto();
        var response = Utils.getProductResponseDto();
        when(productService.update(id, request, multipartFiles)).thenReturn(response);

        var responseEntity = productController.update(id, multipartFiles, request);

        verify(productService).update(eq(id), eq(request), eq(multipartFiles));
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }

    @Test
    void deleteTest() {
        var id = 1L;
        doNothing().when(productService).deleteById(id);

        var responseEntity = productController.delete(id);

        verify(productService).deleteById(eq(id));
        assertEquals(NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
}
