package io.teamchallenge.service;

import io.teamchallenge.repository.BrandRepository;
import io.teamchallenge.service.impl.BrandService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.teamchallenge.util.Utils.getBrand;
import static io.teamchallenge.util.Utils.getBrandRequestDto;
import static io.teamchallenge.util.Utils.getBrandResponseDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;
    @InjectMocks
    private BrandService brandService;

    @Test
    void createTest(){
        var request = getBrandRequestDto();
        var expected = getBrandResponseDto();
        var brand = getBrand();
        brand.setName("Apple");
        when(brandRepository.save(any()))
            .thenReturn(brand);

        var actual = brandService.create(request);

        assertEquals(expected,actual);
    }

    @Test
    void getAllTest(){
        var expected = List.of(getBrandResponseDto());
        var brand = getBrand();
        brand.setName("Apple");
        var brands = List.of(brand);
        when(brandRepository.findAll())
            .thenReturn(brands);

        var actual = brandService.findAll();

        assertEquals(expected,actual);
    }
}
