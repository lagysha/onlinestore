package io.teamchallenge.validator;

import io.teamchallenge.annotation.ValidProductFilterDto;
import io.teamchallenge.dto.filter.ProductFilterDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Validator for product filter DTO.
 * @author Niktia Malov
 */
public class ValidProductFilterDtoValidator implements ConstraintValidator<ValidProductFilterDto, ProductFilterDto> {
    private static final String INVALID_PRICE_RANGE = "Invalid price range";

    /**
     * Validates a ProductFilterDto object based on specific criteria.
     * Checks if the provided ProductFilterDto is valid according to the defined constraints.
     * If the ProductFilterDto is null, the validation is considered successful.
     * Validates the price range within the ProductFilterDto, ensuring that the 'from' price is not greater
     * than the 'to' price.
     * If any validation errors are found, they are collected in a list of exception messages.
     * If there are validation errors, disables the default constraint violation and builds a custom violation message
     * using the collected exception messages.
     *
     * @param productFilterDto The ProductFilterDto object to validate.
     * @param context          The constraint validator context.
     * @return True if the ProductFilterDto is valid or null, false otherwise.
     */
    @Override
    public boolean isValid(ProductFilterDto productFilterDto, ConstraintValidatorContext context) {
        List<String> exceptionsMessages = new ArrayList<>();
        if (Objects.isNull(productFilterDto)) {
            return true;
        }

        if (productFilterDto.getPrice().getFrom() > productFilterDto.getPrice().getTo()) {
            exceptionsMessages.add(INVALID_PRICE_RANGE);
        }

        if (!exceptionsMessages.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.join("\n", exceptionsMessages))
                .addConstraintViolation();
            return false;
        }

        return true;
    }
}
