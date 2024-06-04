package io.teamchallenge.service;

import io.teamchallenge.dto.attributes.AttributeAttributeValueDto;
import io.teamchallenge.dto.attributes.AttributeDto;
import io.teamchallenge.dto.attributes.AttributeValueDto;
import io.teamchallenge.dto.category.CategoryResponseDto;
import io.teamchallenge.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<AttributeAttributeValueDto> getAttributeAttributeValueByCategory(Long categoryId) {
        return categoryRepository.findAllAttributeAttributeValueByCategory(categoryId)
            .collect(Collectors.groupingBy(
                vo -> new AttributeDto(vo.getAttributeId(), vo.getAttributeName()),
                Collectors.mapping(
                    vo -> new AttributeValueDto(vo.getAttributeValueId(), vo.getAttributeValueName()),
                    Collectors.toList()
                )
            ))
            .entrySet()
            .stream()
            .map(entry -> AttributeAttributeValueDto.builder()
                .id(entry.getKey().getId())
                .name(entry.getKey().getName())
                .attributeValueDtos(entry.getValue())
                .build())
            .collect(Collectors.toList());
    }

    public List<CategoryResponseDto> getAll() {
        return categoryRepository.findAll().stream()
            .map(category -> modelMapper.map(category, CategoryResponseDto.class))
            .collect(Collectors.toList());
    }
}
