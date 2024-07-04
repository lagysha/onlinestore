package io.teamchallenge.annotation;

import io.teamchallenge.validator.ImageValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for {@link ImageValidator}.
 * @author Niktia Malov
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Constraint(validatedBy = {ImageValidator.class})
public @interface ImageValidation {
    /**
     * Payload with which the constraint declaration is associated.
     *
     * @return an array of {@link Class} objects that extend {@link Payload}
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * The groups with which the constraint declaration is associated.
     *
     * @return an array of {@link Class} objects
     */
    Class<?>[] groups() default {};

    /**
     * The error message that is shown when the constraint is violated.
     *
     * @return the error message
     */
    String message() default "No more than 5 jpeg, png, jpg files are allowed";
}
