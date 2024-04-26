package io.teamchallenge.handler;

import io.teamchallenge.exception.AlreadyExistsException;
import io.teamchallenge.exception.CreationException;
import io.teamchallenge.exception.ExceptionResponse;
import io.teamchallenge.exception.NotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    private final ErrorAttributes errorAttributes;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException e, WebRequest webRequest){
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(webRequest));
        log.trace(e.getMessage(),e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(exceptionResponse);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleAlreadyExistsException(AlreadyExistsException e, WebRequest webRequest){
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(webRequest));
        log.trace(e.getMessage(),e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(exceptionResponse);
    }

    @ExceptionHandler(CreationException.class)
    public ResponseEntity<ExceptionResponse> handleCreationException(CreationException e, WebRequest webRequest){
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(webRequest));
        log.trace(e.getMessage(),e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(exceptionResponse);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers,
                                                               HttpStatusCode status,
                                                               WebRequest request) {
        HashMap<String,String> map = new HashMap<>();
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();

        errors.forEach((error ->{
            String fieldName= ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            map.put(fieldName,message);
        }));

        return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> getErrorAttributes(WebRequest webRequest) {
        return new HashMap<>(errorAttributes.getErrorAttributes(webRequest,
            ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE)));
    }
}
