package io.teamchallenge.controller;

import io.teamchallenge.service.impl.BrandService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.teamchallenge.utils.Utils.getBrandRequestDto;
import static io.teamchallenge.utils.Utils.getBrandResponseDto;
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
public class BrandControllerTest {

    @InjectMocks
    private BrandController brandController;

    @Mock
    private BrandService brandService;

    @Test
    void createTest(){
        var expected = getBrandResponseDto();
        var request = getBrandRequestDto();
        when(brandService.create(request))
            .thenReturn(expected);

        var actual = brandController.create(request);

        verify(brandService).create(eq(request));
        assertEquals(expected,actual.getBody());
        assertEquals(CREATED,actual.getStatusCode());
    }

    @Test
    void getAllTest(){
        var expected = List.of(getBrandResponseDto());
        when(brandService.findAll())
            .thenReturn(expected);

        var actual = brandController.getAll();

        verify(brandService).findAll();
        assertEquals(expected,actual.getBody());
        assertEquals(OK,actual.getStatusCode());
    }

    @Test
    void deleteTest() {
        Long id = 1L;
        doNothing().when(brandService).deleteById(id);

        var responseEntity = brandController.delete(id);

        verify(brandService).deleteById(eq(id));
        assertEquals(NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void updateTest() {
        Long id = 1L;
        var request = getBrandRequestDto();
        var response = getBrandResponseDto();
        when(brandService.update(id, request)).thenReturn(response);

        var responseEntity =
            brandController.update(id, request);

        verify(brandService).update(eq(id), eq(request));
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }
}
