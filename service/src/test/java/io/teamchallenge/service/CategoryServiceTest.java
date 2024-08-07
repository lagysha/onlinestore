package io.teamchallenge.service;

import io.teamchallenge.dto.attributes.AttributeAttributeValueDto;
import io.teamchallenge.dto.attributes.AttributeValueResponseDto;
import io.teamchallenge.dto.category.CategoryResponseDto;
import io.teamchallenge.entity.Category;
import io.teamchallenge.exception.DeletionException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.CategoryAttributeRepository;
import io.teamchallenge.repository.CategoryRepository;
import io.teamchallenge.service.impl.CategoryService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.teamchallenge.util.Utils.getAttributeAttributeValueVO;
import static io.teamchallenge.util.Utils.getCategory;
import static io.teamchallenge.util.Utils.getCategoryRequestDto;
import static io.teamchallenge.util.Utils.getCategoryResponseDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryAttributeRepository categoryAttributeRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getAttributeAttributeValueByCategoryInProductsTest() {
        Long categoryId = 1L;
        var expected = List.of(AttributeAttributeValueDto
            .builder()
            .id(1L)
            .name("Size")
            .attributeValueResponseDtos(List.of(AttributeValueResponseDto.builder()
                .id(1L)
                .value("Big")
                .build()))
            .build());
        when(categoryRepository.findAllAttributeAttributeValueByCategoryInProducts(categoryId))
            .thenReturn(Stream.of(getAttributeAttributeValueVO()));

        var actual = categoryService.getAttributeAttributeValueByCategoryInProducts(categoryId);

        verify(categoryRepository).findAllAttributeAttributeValueByCategoryInProducts(eq(categoryId));
        assertEquals(expected,actual);
    }

    @Test
    void getAttributeAttributeValueByCategoryTest() {
        Long categoryId = 1L;
        var expected = List.of(AttributeAttributeValueDto
            .builder()
            .id(1L)
            .name("Size")
            .attributeValueResponseDtos(List.of(AttributeValueResponseDto.builder()
                .id(1L)
                .value("Big")
                .build()))
            .build());
        when(categoryAttributeRepository.findAllAttributeAttributeValueByCategory(categoryId))
            .thenReturn(Stream.of(getAttributeAttributeValueVO()));

        var actual = categoryService.getAttributeAttributeValueByCategory(categoryId);

        verify(categoryAttributeRepository).findAllAttributeAttributeValueByCategory(eq(categoryId));
        assertEquals(expected,actual);
    }

    @Test
    void getAllTest() {
        var expected = List.of(getCategoryResponseDto());

        var category = getCategory();
        when(categoryRepository.findAll())
            .thenReturn(List.of(category));
        when(modelMapper.map(category, CategoryResponseDto.class))
            .thenReturn(getCategoryResponseDto());

        var actual = categoryService.findAll();

        verify(categoryRepository).findAll();
        assertEquals(expected,actual);
    }

    @Test
    void createTest(){
        var request = getCategoryRequestDto();
        var expected = getCategoryResponseDto();
        var category = getCategory();
        var categoryToSave = Category
            .builder()
                .name(request.getName())
                    .description(request.getDescription())
                        .build();
        when(categoryRepository.save(categoryToSave))
            .thenReturn(category);

        var actual = categoryService.create(request);

        verify(categoryRepository).save(categoryToSave);
        assertEquals(expected,actual);
    }

    @Test
    void deleteByIdTest() {
        Long id = 1L;

        when(categoryRepository.findById(id)).thenReturn(Optional.of(getCategory()));
        doNothing().when(categoryRepository).deleteById(id);

        categoryService.deleteById(id);

        verify(categoryRepository).findById(eq(id));
        verify(categoryRepository).deleteById(eq(id));
    }

    @Test
    void deleteByIdThrowsNotFoundExceptionWhenNonExistingIdTest() {
        Long id = 1L;

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.deleteById(id));

        verify(categoryRepository).findById(eq(id));
    }

    @Test
    void deleteByIdThrowsDeletionExceptionWhenThereAreRelatedEntitiesToCurrentOneInDatabaseTest() {
        Long id = 1L;

        when(categoryRepository.findById(id)).thenReturn(Optional.of(getCategory()));
        doThrow(new DataIntegrityViolationException("Data integrity violation")).
            when(categoryRepository).deleteById(id);

        assertThrows(DeletionException.class, () -> categoryService.deleteById(id));

        verify(categoryRepository).findById(eq(id));
    }

    @Test
    void updateTest() {
        Long id = 1L;
        var retrievedCategory = getCategory();
        var expected = getCategoryResponseDto();
        var request = getCategoryRequestDto();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(retrievedCategory));
        when(modelMapper.map(retrievedCategory, CategoryResponseDto.class)).thenReturn(expected);

        var actual = categoryService.update(id, request);

        assertEquals(expected, actual);

        verify(categoryRepository).findById(eq(id));
        verify(modelMapper).map(eq(retrievedCategory), eq(CategoryResponseDto.class));
    }

    @Test
    void updateThrowsNotFoundExceptionWhenNonExistingIdTest() {
        Long id = 1L;
        var request = getCategoryRequestDto();

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.update(id, request));

        verify(categoryRepository).findById(eq(id));
    }
}
