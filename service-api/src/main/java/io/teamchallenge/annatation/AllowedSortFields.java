package io.teamchallenge.annatation;

import io.teamchallenge.validator.AllowedSortFieldsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {AllowedSortFieldsValidator.class})
public @interface AllowedSortFields {

    String message() default "Sort field values provided are not within the allowed fields that are sortable.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Specify an array of fields that are allowed.
     *
     * @return the allowed sort fields
     */
    String[] values() default {};
}
