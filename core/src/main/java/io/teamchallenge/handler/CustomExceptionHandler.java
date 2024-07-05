package io.teamchallenge.handler;

import io.teamchallenge.exception.AlreadyExistsException;
import io.teamchallenge.exception.BadCredentialsException;
import io.teamchallenge.exception.BadTokenException;
import io.teamchallenge.exception.ExceptionResponse;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.exception.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for handling exceptions across the whole application.
 * Uses Spring's {@link ControllerAdvice} to provide centralized exception handling.
 * Extends {@link ResponseEntityExceptionHandler} to leverage its handling of standard Spring MVC exceptions.
 * Utilizes {@link ErrorAttributes} to include additional error details in the response.
 * @author Niktia Malov
 * @author Denys Liubchenko
 */
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    private final ErrorAttributes errorAttributes;

    /**
     * Exception handler method to handle NotFoundException.
     *
     * @param e          The NotFoundException instance that occurred.
     * @param webRequest The WebRequest associated with the request.
     * @return A ResponseEntity containing the ExceptionResponse with HttpStatus.NOT_FOUND.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException e, WebRequest webRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(webRequest));
        log.trace(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(exceptionResponse);
    }

    /**
     * Exception handler method to handle NotFoundException.
     *
     * @param e          The NotFoundException instance that occurred.
     * @param webRequest The WebRequest associated with the request.
     * @return A ResponseEntity containing the ExceptionResponse with HttpStatus.NOT_FOUND.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException e,
                                                                           WebRequest webRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(webRequest));
        log.trace(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(exceptionResponse);
    }

    /**
     * Exception handler method to handle AlreadyExistsException.
     *
     * @param e          The AlreadyExistsException instance that occurred.
     * @param webRequest The WebRequest associated with the request.
     * @return A ResponseEntity containing the ExceptionResponse with HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleAlreadyExistsException(AlreadyExistsException e,
                                                                          WebRequest webRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(webRequest));
        log.trace(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(exceptionResponse);
    }

    /**
     * Exception handler method to handle CreationException.
     *
     * @param e          The CreationException instance that occurred.
     * @param webRequest The WebRequest associated with the request.
     * @return A ResponseEntity containing the ExceptionResponse with HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ExceptionResponse> handlePersistenceException(PersistenceException e, WebRequest webRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(webRequest));
        log.trace(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(exceptionResponse);
    }

    /**
     * Override method to handle validation errors in method arguments.
     *
     * @param ex      The MethodArgumentNotValidException instance that occurred.
     * @param headers The HttpHeaders associated with the response.
     * @param status  The HttpStatusCode representing the status of the response.
     * @param request The WebRequest associated with the request.
     * @return A ResponseEntity containing a HashMap of field names and error messages with HttpStatus.BAD_REQUEST.
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers,
                                                               HttpStatusCode status,
                                                               WebRequest request) {
        HashMap<String, String> map = new HashMap<>();
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();

        errors.forEach((error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            map.put(fieldName, message);
        }));

        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    /**
     * Method intercept exception {@link ConstraintViolationException}.
     *
     * @param e          The CreationException instance that occurred.
     * @param webRequest The WebRequest associated with the request.
     * @return A ResponseEntity containing the ExceptionResponse with HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e,
                                                                           WebRequest webRequest) {
        log.warn(e.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(webRequest));
        String detailedMessage = e.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.joining(" "));
        exceptionResponse.setMessage(detailedMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    /**
     * Exception handler method to handle BadTokenException.
     *
     * @param e          The BadTokenException instance that occurred.
     * @param webRequest The WebRequest associated with the request.
     * @return A ResponseEntity containing the ExceptionResponse with HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(BadTokenException.class)
    public ResponseEntity<ExceptionResponse> handleBadTokenException(BadTokenException e, WebRequest webRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(webRequest));
        log.trace(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(exceptionResponse);
    }

    /**
     * Exception handler method to handle BadCredentialsException.
     *
     * @param e          The BadCredentialsException instance that occurred.
     * @param webRequest The WebRequest associated with the request.
     * @return A ResponseEntity containing the ExceptionResponse with HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException e,
                                                                           WebRequest webRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(webRequest));
        log.trace(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(exceptionResponse);
    }

    /**
     * Exception handler method to handle BadRequestException.
     *
     * @param e          The BadRequestException instance that occurred.
     * @param webRequest The WebRequest associated with the request.
     * @return A ResponseEntity containing the ExceptionResponse with HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadRequestException e,
                                                                           WebRequest webRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(webRequest));
        log.trace(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(exceptionResponse);
    }

    /**
     * Exception handler method to handle DeletionException.
     *
     * @param e          The DeletionException instance that occurred.
     * @param webRequest The WebRequest associated with the request.
     * @return A ResponseEntity containing the ExceptionResponse with HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(DeletionException.class)
    public ResponseEntity<ExceptionResponse> handleDeletionException(DeletionException e,
                                                                           WebRequest webRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(webRequest));
        log.trace(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(exceptionResponse);
    }

    /**
     * Exception handler method to handle ConflictException.
     *
     * @param e          The ConflictException instance that occurred.
     * @param webRequest The WebRequest associated with the request.
     * @return A ResponseEntity containing the ExceptionResponse with HttpStatus.CONFLICT.
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionResponse> handleConflictException(ConflictException e, WebRequest webRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(webRequest));
        log.trace(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(exceptionResponse);
    }

    /**
     * Exception handler method to handle ForbiddenException.
     *
     * @param e          The ForbiddenException instance that occurred.
     * @param webRequest The WebRequest associated with the request.
     * @return A ResponseEntity containing the ExceptionResponse with HttpStatus.FORBIDDEN.
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleConflictException(ForbiddenException e, WebRequest webRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(webRequest));
        log.trace(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(exceptionResponse);
    }

    private Map<String, Object> getErrorAttributes(WebRequest webRequest) {
        return new HashMap<>(errorAttributes.getErrorAttributes(webRequest,
            ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE)));
    }
}
