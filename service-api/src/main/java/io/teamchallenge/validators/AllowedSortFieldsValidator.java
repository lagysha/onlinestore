package io.teamchallenge.validators;

import io.teamchallenge.annatations.AllowedSortFields;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

public class AllowedSortFieldsValidator implements ConstraintValidator<AllowedSortFields, Pageable> {

    private List<String> allowedSortFields;
    static final String PROPERTY_NOT_FOUND_MESSAGE = "The following sort fields [%s] are not within the allowed fields. "
        + "Allowed sort fields are: [%s]";

    @Override
    public void initialize(AllowedSortFields constraintAnnotation) {
        allowedSortFields = Arrays.asList(constraintAnnotation.values());
    }

    @Override
    public boolean isValid(Pageable pageable, ConstraintValidatorContext context) {
        if(pageable == null){
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
        context.buildConstraintViolationWithTemplate(String.format(PROPERTY_NOT_FOUND_MESSAGE, notAllowedFields, String.join(",", allowedSortFields)))
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
