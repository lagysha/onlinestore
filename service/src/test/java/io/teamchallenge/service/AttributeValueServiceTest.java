package io.teamchallenge.service;

import io.teamchallenge.constant.ExceptionMessage;
import io.teamchallenge.exception.DeletionException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.AttributeValueRepository;
import io.teamchallenge.service.impl.AttributeValueService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static io.teamchallenge.util.Utils.getAttributeValue;
import static io.teamchallenge.util.Utils.getAttributeValuePatchRequestDto;
import static io.teamchallenge.util.Utils.getAttributeValueResponseDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AttributeValueServiceTest {

    @Mock
    private AttributeValueRepository attributeValueRepository;
    @InjectMocks
    private AttributeValueService attributeValueService;

    @Test
    void deleteByIdTest() {
        Long id = 1L;

        when(attributeValueRepository.findById(id)).thenReturn(Optional.of(getAttributeValue()));
        doNothing().when(attributeValueRepository).deleteById(id);

        attributeValueService.deleteById(id);

        verify(attributeValueRepository).findById(eq(id));
        verify(attributeValueRepository).deleteById(eq(id));
    }

    @Test
    void deleteByIdThrowsNotFoundExceptionWhenNonExistingIdTest() {
        Long id = 1L;

        when(attributeValueRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> attributeValueService.deleteById(id));

        verify(attributeValueRepository).findById(eq(id));
    }

    @Test
    void deleteByIdThrowsDeletionExceptionWhenThereAreRelatedEntitiesToCurrentOneInDatabaseTest() {
        Long id = 1L;

        when(attributeValueRepository.findById(id)).thenReturn(Optional.of(getAttributeValue()));
        doThrow(new DataIntegrityViolationException("Data integrity violation")).
            when(attributeValueRepository).deleteById(id);

        assertThrows(DeletionException.class, () -> attributeValueService.deleteById(id));

        verify(attributeValueRepository).findById(eq(id));
    }

    @Test
    void updateTest() {
        Long id = 1L;
        var retrievedAttribute = getAttributeValue();
        var expected = getAttributeValueResponseDto();
        var request = getAttributeValuePatchRequestDto();

        when(attributeValueRepository.findById(id)).thenReturn(Optional.of(retrievedAttribute));

        var actual = attributeValueService.patchUpdate(id, request);

        assertEquals(expected, actual);

        verify(attributeValueRepository).findById(eq(id));
    }

    @Test
    void updateThrowsNotFoundExceptionWhenNonExistingIdTest() {
        Long id = 1L;
        var request = getAttributeValuePatchRequestDto();

        when(attributeValueRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> attributeValueService.patchUpdate(id, request),
            ExceptionMessage.ATTRIBUTEVALUE_NOT_FOUND_BY_ID.formatted(1L));

        verify(attributeValueRepository).findById(eq(id));
    }
}
