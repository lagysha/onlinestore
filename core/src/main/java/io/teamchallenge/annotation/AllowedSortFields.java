package io.teamchallenge.annotation;

import io.teamchallenge.validator.AllowedSortFieldsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Niktia Malov
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {AllowedSortFieldsValidator.class})
public @interface AllowedSortFields {
    String message() default "Sort field values provided are not within the allowed fields that are sortable.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] values() default {};
}
