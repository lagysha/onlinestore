package io.teamchallenge.controller;

import io.teamchallenge.service.impl.AttributeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.teamchallenge.utils.Utils.getAttributeRequestDto;
import static io.teamchallenge.utils.Utils.getAttributeRequestUpdateDto;
import static io.teamchallenge.utils.Utils.getAttributeResponseDto;
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
public class AttributeControllerTest {

    @InjectMocks
    private AttributeController attributeController;

    @Mock
    private AttributeService attributeService;

    @Test
    void createTest(){
        var expected = getAttributeResponseDto();
        var request = getAttributeRequestDto();
        when(attributeService.create(request))
            .thenReturn(expected);

        var actual = attributeController.create(request);

        verify(attributeService).create(eq(request));
        assertEquals(expected,actual.getBody());
        assertEquals(CREATED,actual.getStatusCode());
    }

    @Test
    void deleteTest() {
        Long id = 1L;
        doNothing().when(attributeService).deleteById(id);

        var responseEntity = attributeController.delete(id);

        verify(attributeService).deleteById(eq(id));
        assertEquals(NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void updateTest() {
        Long id = 1L;
        var request = getAttributeRequestUpdateDto();
        var response = getAttributeResponseDto();
        when(attributeService.update(id, request)).thenReturn(response);

        var responseEntity =
            attributeController.update(id, request);

        verify(attributeService).update(eq(id), eq(request));
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }
}
