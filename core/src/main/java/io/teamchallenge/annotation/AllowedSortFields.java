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
 * Annotation for {@link AllowedSortFieldsValidator}.
 * @author Niktia Malov
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {AllowedSortFieldsValidator.class})
public @interface AllowedSortFields {
    /**
     * The error message to be returned when the sort field values are not within the allowed sortable fields.
     *
     * @return the error message
     */
    String message() default "Sort field values provided are not within the allowed fields that are sortable.";


    /**
     * Defines the validation groups that this constraint belongs to.
     * The default value is an empty array, indicating that this constraint belongs to no specific group.
     *
     * @return An array of classes representing the validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * Defines the payload associated with the constraint.
     * The default value is an empty array, indicating that no additional payload is associated with the constraint.
     *
     * @return An array of classes representing the payload.
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * The list of allowed sortable field values.
     *
     * @return the array of allowed sortable field values
     */
    String[] values() default {};
}
