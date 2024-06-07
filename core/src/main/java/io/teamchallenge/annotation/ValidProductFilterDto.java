package io.teamchallenge.annotation;

import io.teamchallenge.validator.ValidProductFilterDtoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for {@link ValidProductFilterDtoValidator}.
 * @author Niktia Malov
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Constraint(validatedBy = {ValidProductFilterDtoValidator.class})
public @interface ValidProductFilterDto {
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
     * The error message to be returned when the product filter DTO is invalid.
     */
    String message() default "Invalid product filter dto";
}
