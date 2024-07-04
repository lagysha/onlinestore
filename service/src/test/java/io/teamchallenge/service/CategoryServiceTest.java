package io.teamchallenge.service;

import io.teamchallenge.dto.attributes.AttributeAttributeValueDto;
import io.teamchallenge.dto.attributes.AttributeValueDto;
import io.teamchallenge.dto.category.CategoryResponseDto;
import io.teamchallenge.repository.CategoryAttributeRepository;
import io.teamchallenge.repository.CategoryRepository;
import io.teamchallenge.service.impl.CategoryService;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.teamchallenge.util.Utils.getAttributeAttributeValueVO;
import static io.teamchallenge.util.Utils.getBrand;
import static io.teamchallenge.util.Utils.getBrandRequestDto;
import static io.teamchallenge.util.Utils.getBrandResponseDto;
import static io.teamchallenge.util.Utils.getCategory;
import static io.teamchallenge.util.Utils.getCategoryRequestDto;
import static io.teamchallenge.util.Utils.getCategoryResponseDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
            .attributeValueDtos(List.of(AttributeValueDto.builder()
                .id(1L)
                .name("Big")
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
            .attributeValueDtos(List.of(AttributeValueDto.builder()
                .id(1L)
                .name("Big")
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
        when(categoryRepository.save(any()))
            .thenReturn(category);

        var actual = categoryService.create(request);

        assertEquals(expected,actual);
    }
}
