package io.teamchallenge.annotation;

import io.teamchallenge.validator.AllowedSortFieldsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {AllowedSortFieldsValidator.class})
public @interface AllowedSortFields {
    /**
     * Specifies the message to be used in case the validation fails.
     *
     * @return The message associated with the constraint.
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
     * Specify an array of fields that are allowed.
     *
     * @return the allowed sort fields
     */
    String[] values() default {};
}
