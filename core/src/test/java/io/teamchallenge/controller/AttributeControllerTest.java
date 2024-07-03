package io.teamchallenge.controller;

import io.teamchallenge.service.impl.AttributeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.teamchallenge.utils.Utils.getAttributeRequestDto;
import static io.teamchallenge.utils.Utils.getAttributeResponseDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;

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
}
