package io.teamchallenge.controller;

import io.teamchallenge.service.impl.CategoryService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.teamchallenge.utils.Utils.getAttributeAttributeValueDto;
import static io.teamchallenge.utils.Utils.getCategoryResponseDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

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
        when(categoryService.getAll())
            .thenReturn(expected);

        var actual = categoryController.getAll();

        verify(categoryService).getAll();
        assertEquals(expected,actual.getBody());
        assertEquals(OK,actual.getStatusCode());
    }
}
