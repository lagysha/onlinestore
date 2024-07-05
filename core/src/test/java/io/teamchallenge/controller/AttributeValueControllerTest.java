package io.teamchallenge.controller;

import io.teamchallenge.service.impl.AttributeValueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.teamchallenge.utils.Utils.getAttributeValuePatchRequestDto;
import static io.teamchallenge.utils.Utils.getAttributeValueResponseDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class AttributeValueControllerTest {

    @InjectMocks
    private AttributeValueController attributeValueController;

    @Mock
    private AttributeValueService attributeValueService;

    @Test
    void deleteTest() {
        Long id = 1L;
        doNothing().when(attributeValueService).deleteById(id);

        var responseEntity = attributeValueController.delete(id);

        verify(attributeValueService).deleteById(eq(id));
        assertEquals(NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void updateTest() {
        Long id = 1L;
        var request = getAttributeValuePatchRequestDto();
        var response = getAttributeValueResponseDto();
        when(attributeValueService.patchUpdate(id, request)).thenReturn(response);

        var responseEntity =
            attributeValueController.patchUpdate(id, request);

        verify(attributeValueService).patchUpdate(eq(id), eq(request));
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }
}
