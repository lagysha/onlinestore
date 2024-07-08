package io.teamchallenge.service;

import io.teamchallenge.entity.attributes.Attribute;
import io.teamchallenge.entity.attributes.CategoryAttribute;
import io.teamchallenge.exception.DeletionException;
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

import static io.teamchallenge.constant.ExceptionMessage.CATEGORY_NOT_FOUND_BY_ID;
import static io.teamchallenge.util.Utils.getAttribute;
import static io.teamchallenge.util.Utils.getAttributeRequestDto;
import static io.teamchallenge.util.Utils.getAttributeRequestUpdateDto;
import static io.teamchallenge.util.Utils.getAttributeResponseDto;
import static io.teamchallenge.util.Utils.getCategory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
        var categoryAttributeToSave = CategoryAttribute.builder()
            .category(category)
            .attribute(attribute)
            .build();
        var attributeCategory = CategoryAttribute.builder()
            .id(1L)
            .category(category)
            .attribute(attribute)
            .build();
        when(attributeRepository.findByName(request.getName())).thenReturn(Optional.of(attribute));
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));
        when(categoryAttributeRepository.save(categoryAttributeToSave)).thenReturn(attributeCategory);

        var actual = attributeService.create(request);

        verify(attributeRepository).findByName(eq(request.getName()));
        verify(categoryRepository).findById(eq(request.getCategoryId()));
        verify(categoryAttributeRepository).save(categoryAttributeToSave);
        assertEquals(expected,actual);
    }

    @Test
    void createWithNotExistingAttributeTest(){
        var request = getAttributeRequestDto();
        var expected = getAttributeResponseDto();
        var attribute = getAttribute();
        attribute.setName("Color");
        var category = getCategory();
        var attributeToSave = Attribute.builder()
            .name("Color")
            .build();
        var categoryAttributeToSave = CategoryAttribute.builder()
            .category(category)
            .attribute(attribute)
            .build();
        var attributeCategory = CategoryAttribute.builder()
            .id(1L)
            .category(category)
            .attribute(attribute)
            .build();
        when(attributeRepository.findByName(request.getName())).thenReturn(Optional.empty());
        when(attributeRepository.save(attributeToSave)).thenReturn(attribute);
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));
        when(categoryAttributeRepository.save(categoryAttributeToSave)).thenReturn(attributeCategory);

        var actual = attributeService.create(request);

        verify(attributeRepository).findByName(eq(request.getName()));
        verify(categoryRepository).findById(eq(request.getCategoryId()));
        verify(categoryAttributeRepository).save(categoryAttributeToSave);
        verify(attributeRepository).save(attributeToSave);
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

        assertThrows(PersistenceException.class,() -> attributeService.create(request));

        verify(attributeRepository).findByName(eq(request.getName()));
        verify(categoryRepository).findById(eq(request.getCategoryId()));
        verify(categoryAttributeRepository).save(any());
    }

    @Test
    void deleteByIdTest() {
        Long id = 1L;

        when(attributeRepository.findById(id)).thenReturn(Optional.of(getAttribute()));
        doNothing().when(attributeRepository).deleteById(id);

        attributeService.deleteById(id);

        verify(attributeRepository).findById(eq(id));
        verify(attributeRepository).deleteById(eq(id));
    }

    @Test
    void deleteByIdThrowsNotFoundExceptionWhenNonExistingIdTest() {
        Long id = 1L;

        when(attributeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> attributeService.deleteById(id));

        verify(attributeRepository).findById(eq(id));
    }

    @Test
    void deleteByIdThrowsDeletionExceptionWhenThereAreRelatedEntitiesToCurrentOneInDatabaseTest() {
        Long id = 1L;

        when(attributeRepository.findById(id)).thenReturn(Optional.of(getAttribute()));
        doThrow(new DataIntegrityViolationException("Data integrity violation")).
            when(attributeRepository).deleteById(id);

        assertThrows(DeletionException.class, () -> attributeService.deleteById(id));

        verify(attributeRepository).findById(eq(id));
    }

    @Test
    void updateTest() {
        Long id = 1L;
        var retrievedAttribute = getAttribute();
        var expected = getAttributeResponseDto();
        var request = getAttributeRequestUpdateDto();

        when(attributeRepository.findById(id)).thenReturn(Optional.of(retrievedAttribute));

        var actual = attributeService.update(id, request);

        assertEquals(expected, actual);

        verify(attributeRepository).findById(eq(id));
    }

    @Test
    void updateThrowsNotFoundExceptionWhenNonExistingIdTest() {
        Long id = 1L;
        var request = getAttributeRequestUpdateDto();

        when(attributeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> attributeService.update(id, request));

        verify(attributeRepository).findById(eq(id));
    }
}
