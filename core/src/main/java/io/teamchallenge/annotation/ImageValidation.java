package io.teamchallenge.annotation;

import io.teamchallenge.validator.ImageValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Constraint(validatedBy = {ImageValidator.class})
public @interface ImageValidation {
    Class<? extends Payload> [] payload() default{};
    Class<?>[] groups() default {};
    String message() default "No more than 5 jpeg,png,jpg files are allowed";
}
