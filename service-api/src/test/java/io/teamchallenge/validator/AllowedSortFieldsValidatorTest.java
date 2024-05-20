package io.teamchallenge.validator;


import io.teamchallenge.annotation.AllowedSortFields;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class AllowedSortFieldsValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    @InjectMocks
    private AllowedSortFieldsValidator allowedSortFieldsValidator;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    private final AllowedSortFields allowedSortFields = getAllowedSortFields();;
    private boolean initialized = false;

    @BeforeEach
    void setUp() {
        if (!initialized) {
            allowedSortFieldsValidator.initialize(allowedSortFields);
            initialized = true;
        }
    }

    @Test
    void initializeTest(){
        allowedSortFieldsValidator.initialize(allowedSortFields);
    }

    @Test
    void isValidWithNullPageableReturnTrueTest(){
        assertTrue(allowedSortFieldsValidator.isValid(null,context));
    }

    @Test
    void isValidWithUnsortedPageableReturnTrueTest(){
        assertTrue(allowedSortFieldsValidator.isValid(Pageable.ofSize(1),context));
    }

    @Test
    void isValidWithAllowedFieldsReturnTrueTest(){
        assertTrue(allowedSortFieldsValidator.isValid(Pageable.unpaged(Sort.by("1","2")),context));
    }

    @Test
    void isValidWithNotAllowedFieldsReturnFalseTest(){
        when(context.buildConstraintViolationWithTemplate(anyString()))
            .thenReturn(constraintViolationBuilder);

        assertFalse(allowedSortFieldsValidator.isValid(Pageable.unpaged(Sort.by("1","2","3")),context));
    }

    private static AllowedSortFields getAllowedSortFields() {
        return new AllowedSortFields() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String message() {
                return null;
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public String[] values() {
                return new String[] {"1", "2"};
            }
        };
    }
}
