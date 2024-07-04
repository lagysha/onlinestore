package io.teamchallenge.annotation;

import io.teamchallenge.validator.OrderRequestValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = OrderRequestValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOrderRequest {
    /**
     * Specifies the message to be used in case the validation fails.
     *
     * @return The message associated with the constraint.
     */
    String message() default "Order must contain address if delivery method is COURIER "
                             + "or else order must contain post address.";

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
}
