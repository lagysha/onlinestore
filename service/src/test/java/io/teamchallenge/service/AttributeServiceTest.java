package io.teamchallenge.service;

import io.teamchallenge.dto.attributes.AttributeResponseDto;
import io.teamchallenge.entity.attributes.Attribute;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.exception.PersistenceException;
import io.teamchallenge.repository.AttributeRepository;
import io.teamchallenge.repository.CategoryAttributeRepository;
import io.teamchallenge.repository.CategoryRepository;
import io.teamchallenge.service.impl.AttributeService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static io.teamchallenge.constant.ExceptionMessage.ATTRIBUTE_PERSISTENCE_EXCEPTION;
import static io.teamchallenge.constant.ExceptionMessage.CATEGORY_NOT_FOUND_BY_ID;
import static io.teamchallenge.util.Utils.getAttribute;
import static io.teamchallenge.util.Utils.getAttributeRequestDto;
import static io.teamchallenge.util.Utils.getAttributeResponseDto;
import static io.teamchallenge.util.Utils.getCategory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AttributeServiceTest {

    @Mock
    private CategoryAttributeRepository categoryAttributeRepository;
    @Mock
    private AttributeRepository attributeRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private AttributeService attributeService;

    @Test
    void createTest(){
        var request = getAttributeRequestDto();
        var expected = getAttributeResponseDto();
        var attribute = getAttribute();
        attribute.setName("Color");
        var category = getCategory();
        when(attributeRepository.findByName(request.getName())).thenReturn(Optional.of(attribute));
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));
        when(categoryAttributeRepository.save(any())).thenReturn(null);

        var actual = attributeService.create(request);

        verify(attributeRepository).findByName(eq(request.getName()));
        verify(categoryRepository).findById(eq(request.getCategoryId()));
        verify(categoryAttributeRepository).save(any());
        assertEquals(expected,actual);
    }

    @Test
    void createWithNotExistingAttributeTest(){
        var request = getAttributeRequestDto();
        var expected = getAttributeResponseDto();
        var attribute = getAttribute();
        attribute.setName("Color");
        var category = getCategory();
        when(attributeRepository.findByName(request.getName())).thenReturn(Optional.empty());
        when(attributeRepository.save(any())).thenReturn(attribute);
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));
        when(categoryAttributeRepository.save(any())).thenReturn(null);

        var actual = attributeService.create(request);

        verify(attributeRepository).findByName(eq(request.getName()));
        verify(categoryRepository).findById(eq(request.getCategoryId()));
        verify(categoryAttributeRepository).save(any());
        verify(attributeRepository).save(any());
        assertEquals(expected,actual);
    }

    @Test
    void createWithNotExistingCategoryThrowsNotFoundExceptionTest(){
        var request = getAttributeRequestDto();
        var attribute = getAttribute();
        attribute.setName("Color");
        when(attributeRepository.findByName(request.getName())).thenReturn(Optional.empty());
        when(attributeRepository.save(any())).thenReturn(attribute);
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,() -> attributeService.create(request)
            ,CATEGORY_NOT_FOUND_BY_ID.formatted(request.getCategoryId()));

        verify(attributeRepository).findByName(eq(request.getName()));
        verify(categoryRepository).findById(eq(request.getCategoryId()));
        verify(attributeRepository).save(any());
    }

    @Test
    void createWithExistingPairOfCategoryAndAttributeThrowsPersistenceExceptionTest(){
        var request = getAttributeRequestDto();
        var attribute = getAttribute();
        attribute.setName("Color");
        var category = getCategory();
        when(attributeRepository.findByName(request.getName())).thenReturn(Optional.of(attribute));
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));
        when(categoryAttributeRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(PersistenceException.class,() -> attributeService.create(request)
            ,ATTRIBUTE_PERSISTENCE_EXCEPTION);

        verify(attributeRepository).findByName(eq(request.getName()));
        verify(categoryRepository).findById(eq(request.getCategoryId()));
        verify(categoryAttributeRepository).save(any());
    }
}
