package io.teamchallenge.annotation;

import io.teamchallenge.validator.ValidProductFilterDtoValidator;
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
@Constraint(validatedBy = {ValidProductFilterDtoValidator.class})
public @interface ValidProductFilterDto {
    Class<? extends Payload> [] payload() default{};
    Class<?>[] groups() default {};
    String message() default "Invalid product filter dto";
}
