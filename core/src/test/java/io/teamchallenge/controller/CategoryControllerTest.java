package io.teamchallenge.controller;

import io.teamchallenge.service.impl.CategoryService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.teamchallenge.utils.Utils.getAttributeAttributeValueDto;
import static io.teamchallenge.utils.Utils.getCategoryRequestDto;
import static io.teamchallenge.utils.Utils.getCategoryResponseDto;
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
public class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @Test
    void getAttributeAttributeValueInProductsTest(){
        Long categoryId = 1L;
        var expected = List.of(getAttributeAttributeValueDto());
        when(categoryService.getAttributeAttributeValueByCategoryInProducts(categoryId))
            .thenReturn(expected);

        var actual = categoryController.getAttributeAttributeValueInProducts(categoryId);

        verify(categoryService).getAttributeAttributeValueByCategoryInProducts(eq(categoryId));
        assertEquals(expected,actual.getBody());
        assertEquals(OK,actual.getStatusCode());
    }

    @Test
    void getAttributeAttributeValueTest(){
        Long categoryId = 1L;
        var expected = List.of(getAttributeAttributeValueDto());
        when(categoryService.getAttributeAttributeValueByCategory(categoryId))
            .thenReturn(expected);

        var actual = categoryController.getAttributeAttributeValue(categoryId);

        verify(categoryService).getAttributeAttributeValueByCategory(eq(categoryId));
        assertEquals(expected,actual.getBody());
        assertEquals(OK,actual.getStatusCode());
    }

    @Test
    void getAllTest(){
        var expected = List.of(getCategoryResponseDto());
        when(categoryService.findAll())
            .thenReturn(expected);

        var actual = categoryController.getAll();

        verify(categoryService).findAll();
        assertEquals(expected,actual.getBody());
        assertEquals(OK,actual.getStatusCode());
    }

    @Test
    void createTest(){
        var expected = getCategoryResponseDto();
        var request = getCategoryRequestDto();
        when(categoryService.create(request))
            .thenReturn(expected);

        var actual = categoryController.create(request);

        verify(categoryService).create(eq(request));
        assertEquals(expected,actual.getBody());
        assertEquals(CREATED,actual.getStatusCode());
    }

    @Test
    void deleteTest() {
        Long id = 1L;
        doNothing().when(categoryService).deleteById(id);

        var responseEntity = categoryController.delete(id);

        verify(categoryService).deleteById(eq(id));
        assertEquals(NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void updateTest() {
        Long id = 1L;
        var request = getCategoryRequestDto();
        var response = getCategoryResponseDto();
        when(categoryService.update(id, request)).thenReturn(response);

        var responseEntity =
            categoryController.update(id, request);

        verify(categoryService).update(eq(id), eq(request));
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }
}
