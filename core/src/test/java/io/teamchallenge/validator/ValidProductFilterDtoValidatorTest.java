package io.teamchallenge.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.teamchallenge.utils.Utils.getProductFilterDto;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ValidProductFilterDtoValidatorTest {
    @InjectMocks
    private ValidProductFilterDtoValidator validProductFilterDtoValidator;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;
    @Mock
    private ConstraintValidatorContext context;

    @Test
    void isValidReturnsTrueWhenProductFilterDtoIsNull(){
        assertTrue(validProductFilterDtoValidator.isValid(null,context));
    }

    @Test
    void isValid(){
        assertTrue(validProductFilterDtoValidator.isValid(getProductFilterDto(),context));
    }

    @Test
    void isValidReturnsFalseWithInvalidPriceRange(){
        var invalidProductFilterDto = getProductFilterDto();
        invalidProductFilterDto.getPrice().setFrom(11111111);
        when(context.buildConstraintViolationWithTemplate(anyString()))
            .thenReturn(constraintViolationBuilder);

        assertFalse(validProductFilterDtoValidator.isValid(invalidProductFilterDto,context));
    }
}
