package io.teamchallenge.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.core.annotation.CurrentSecurityContext;

/**
 * Annotation is used for injecting id of user from claims.
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@CurrentSecurityContext(expression = "authentication.getClaims().get('id')")
public @interface CurrentUserId {
}
