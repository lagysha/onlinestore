package io.teamchallenge.validator;

import io.teamchallenge.annotation.AllowedSortFields;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Validator for sort fields.
 * @author Niktia Malov
 */
public class AllowedSortFieldsValidator implements ConstraintValidator<AllowedSortFields, Pageable> {
    static final String PROPERTY_NOT_FOUND_MESSAGE = "The following sort fields [%s] are not within the allowed fields."
        + " Allowed sort fields are: [%s]";
    private List<String> allowedSortFields;

    /**
     * Initializes the validator with the values specified in the AllowedSortFields annotation.
     *
     * @param constraintAnnotation The AllowedSortFields annotation containing the values to initialize the validator.
     */
    @Override
    public void initialize(AllowedSortFields constraintAnnotation) {
        allowedSortFields = Arrays.asList(constraintAnnotation.values());
    }

    /**
     * Validates the Pageable object to ensure that the sort fields are allowed.
     *
     * @param pageable The Pageable object containing sorting information.
     * @param context  The ConstraintValidatorContext for applying the constraint.
     * @return True if the Pageable object is valid (i.e., sort fields are allowed), false otherwise.
     */
    @Override
    public boolean isValid(Pageable pageable, ConstraintValidatorContext context) {
        if (pageable == null) {
            return true;
        }

        Sort sort = pageable.getSort();
        if (sort.isUnsorted()) {
            return true;
        }

        String notAllowedFields = fieldsNotFoundAsCommaDelimited(sort);

        if (notAllowedFields.isEmpty()) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                String.format(PROPERTY_NOT_FOUND_MESSAGE, notAllowedFields, String.join(",", allowedSortFields)))
            .addConstraintViolation();

        return false;
    }

    private String fieldsNotFoundAsCommaDelimited(Sort sort) {
        return sort
            .stream()
            .map(Sort.Order::getProperty)
            .filter(s -> !allowedSortFields.contains(s))
            .collect(Collectors.joining(","));
    }
}
