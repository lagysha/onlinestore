package io.teamchallenge.validator;

import io.teamchallenge.annotation.ValidProductFilterDto;
import io.teamchallenge.dto.filter.ProductFilterDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ValidProductFilterDtoValidator implements ConstraintValidator<ValidProductFilterDto, ProductFilterDto> {
    private static final String INVALID_PRICE_RANGE = "Invalid price range";

    @Override
    public boolean isValid(ProductFilterDto productFilterDto, ConstraintValidatorContext context) {
        List<String> exceptionsMessages = new ArrayList<>();
        if(Objects.isNull(productFilterDto)){
            return true;
        }

        if(productFilterDto.getPrice().getFrom() > productFilterDto.getPrice().getTo()){
            exceptionsMessages.add(INVALID_PRICE_RANGE);
        }

        if(!exceptionsMessages.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.join("\n", exceptionsMessages))
                .addConstraintViolation();
            return false;
        }

        return true;
    }
}
