package io.teamchallenge.handler;

import io.teamchallenge.exception.AlreadyExistsException;
import io.teamchallenge.exception.ExceptionResponse;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.exception.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

@ExtendWith(MockitoExtension.class)
public class CustomExceptionHandlerTest {

    @Mock
    private WebRequest webRequest;
    @Mock
    private ErrorAttributes errorAttributes;
    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private CustomExceptionHandler customExceptionHandler;
    private Map<String, Object> objectMap;
    @Mock
    private ConstraintViolation<String> constraintViolation;

    @BeforeEach
    void init() {
        objectMap = new HashMap<>();
        objectMap.put("path", "path");
        objectMap.put("message", "test");
        objectMap.put("timestamp", "2024-05-13T12:45:30.123+0300");
        objectMap.put("trace", "Test Server Error");
    }

    @Test
    void handleNotFoundExceptionTest() {
        NotFoundException notFoundException = new NotFoundException("test");
        ExceptionResponse exceptionResponse = new ExceptionResponse(objectMap);
        when(errorAttributes.getErrorAttributes(eq(webRequest),
            any(ErrorAttributeOptions.class))).thenReturn(objectMap);

        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse)
            , customExceptionHandler.handleNotFoundException(notFoundException, webRequest));
    }

    @Test
    void handleAlreadyExistsExceptionTest() {
        AlreadyExistsException alreadyExistsException = new AlreadyExistsException("test");
        ExceptionResponse exceptionResponse = new ExceptionResponse(objectMap);
        when(errorAttributes.getErrorAttributes(eq(webRequest),
            any(ErrorAttributeOptions.class))).thenReturn(objectMap);

        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse)
            , customExceptionHandler.handleAlreadyExistsException(alreadyExistsException, webRequest));
    }

    @Test
    void handlePersistenceExceptionTest() {
        PersistenceException persistenceException = new PersistenceException("test");
        ExceptionResponse exceptionResponse = new ExceptionResponse(objectMap);
        when(errorAttributes.getErrorAttributes(eq(webRequest),
            any(ErrorAttributeOptions.class))).thenReturn(objectMap);

        assertEquals(customExceptionHandler.handlePersistenceException(persistenceException, webRequest),
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse));
    }

    @Test
    void handleMethodArgumentNotValidTest() {
        List<ObjectError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("objectName", "fieldName1", "Error message 1"));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
            null, bindingResult);
        HashMap<String, String> map = new HashMap<>();
        map.put("fieldName1", "Error message 1");

        when(bindingResult.getAllErrors()).thenReturn(fieldErrors);

        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map),
            customExceptionHandler.handleMethodArgumentNotValid(exception, null, null, webRequest));
    }

    @Test
    void handleConstraintViolationExceptionTest() {
        HashSet<ConstraintViolation<?>> constraintViolations = new HashSet<>();
        constraintViolations.add(constraintViolation);
        ConstraintViolationException constraintViolationException =
            new ConstraintViolationException(constraintViolations);
        ExceptionResponse exceptionResponse = new ExceptionResponse(objectMap);
        exceptionResponse.setMessage("myField cannot be blank");

        when(constraintViolation.getMessage()).thenReturn("myField cannot be blank");
        when(errorAttributes.getErrorAttributes(eq(webRequest),
            any(ErrorAttributeOptions.class))).thenReturn(objectMap);

        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse),
            customExceptionHandler.handleConstraintViolationException(constraintViolationException, webRequest));
    }
}