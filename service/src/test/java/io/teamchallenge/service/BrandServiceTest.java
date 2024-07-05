package io.teamchallenge.service;

import io.teamchallenge.constant.ExceptionMessage;
import io.teamchallenge.exception.DeletionException;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.BrandRepository;
import io.teamchallenge.service.impl.BrandService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static io.teamchallenge.util.Utils.getAttribute;
import static io.teamchallenge.util.Utils.getBrand;
import static io.teamchallenge.util.Utils.getBrandRequestDto;
import static io.teamchallenge.util.Utils.getBrandResponseDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
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

    @Test
    void deleteByIdTest() {
        Long id = 1L;

        when(brandRepository.findById(id)).thenReturn(Optional.of(getBrand()));
        doNothing().when(brandRepository).deleteById(id);

        brandService.deleteById(id);

        verify(brandRepository).findById(eq(id));
        verify(brandRepository).deleteById(eq(id));
    }

    @Test
    void deleteByIdThrowsNotFoundExceptionWhenNonExistingIdTest() {
        Long id = 1L;

        when(brandRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> brandService.deleteById(id));

        verify(brandRepository).findById(eq(id));
    }

    @Test
    void deleteByIdThrowsDeletionExceptionWhenThereAreRelatedEntitiesToCurrentOneInDatabaseTest() {
        Long id = 1L;

        when(brandRepository.findById(id)).thenReturn(Optional.of(getBrand()));
        doThrow(new DataIntegrityViolationException("Data integrity violation")).
            when(brandRepository).deleteById(id);

        assertThrows(DeletionException.class, () -> brandService.deleteById(id));

        verify(brandRepository).findById(eq(id));
    }

    @Test
    void updateTest() {
        Long id = 1L;
        var retrievedBrand = getBrand();
        var expected = getBrandResponseDto();
        var request = getBrandRequestDto();

        when(brandRepository.findById(id)).thenReturn(Optional.of(retrievedBrand));

        var actual = brandService.update(id, request);

        assertEquals(expected, actual);

        verify(brandRepository).findById(eq(id));
    }

    @Test
    void updateThrowsNotFoundExceptionWhenNonExistingIdTest() {
        Long id = 1L;
        var request = getBrandRequestDto();

        when(brandRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> brandService.update(id, request));

        verify(brandRepository).findById(eq(id));
    }
}
