package org.example.stage1.exception;

import org.example.stage1.response.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler, this class is used to handle exceptions globally
 * instead of handling them in each controller separately.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * takes care of the exception when a resource is not found, 404 Not Found
     */
    @ExceptionHandler(NotExists.class)
    public ResponseEntity<StandardResponse> handleNotExists(NotExists ex, WebRequest request) {

        // Create a map to hold the error details
        Map<String, String> details = new HashMap<>();
        details.put("type", "Resource Not Found");
        details.put("message", ex.getMessage());

        // Create a StandardResponse object with the error details
        StandardResponse response = new StandardResponse("error", null, details);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * takes care of the exception when a resource already exists, 409 Conflict
     * This is more appropriate than 400 Bad Request when trying to create a resource with an ID that already exists
     */
    @ExceptionHandler(AlreadyExists.class)
    public ResponseEntity<StandardResponse> handleAlreadyExists(AlreadyExists ex, WebRequest request) {

        // Create a map to hold the error details
        Map<String, String> details = new HashMap<>();
        details.put("type", "Resource Conflict");
        details.put("message", ex.getMessage());

        // Create a StandardResponse object with the error details
        StandardResponse response = new StandardResponse("error", null, details);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * takes care of the exception when there's an ID mismatch, 400 Bad Request
     */
    @ExceptionHandler(StudentIdAndIdMismatch.class)
    public ResponseEntity<StandardResponse> handleIdMismatch(StudentIdAndIdMismatch ex, WebRequest request) {

        // Create a map to hold the error details
        Map<String, String> details = new HashMap<>();
        details.put("type", "ID Mismatch");
        details.put("message", ex.getMessage());

        // Create a StandardResponse object with the error details
        StandardResponse response = new StandardResponse("error", null, details);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler for @Valid validation errors, such as @NotNull, @Size, etc.
     * BadRequest 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {

        // Collect validation errors into a map, where keys are field names and values are error messages
        // there can be multiple errors for different fields
        Map<String, String> errors = new HashMap<>();

        // Iterate over all field errors and populate the map
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        // Create a response with the error details
        // The response will contain a type and a map of field errors
        Map<String, Object> details = new HashMap<>();
        details.put("type", "Validation Failed");
        details.put("fields", errors);

        // Create a StandardResponse object with the error details
        StandardResponse response = new StandardResponse("error", null, details);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * takes care of general exceptions, 500 Internal Server Error
     * This is a catch-all handler for any unhandled exceptions
     * It should be used as a last resort
     * error 500 Internal Server Error, which indicates that something went wrong on the server side
     *
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse> handleGenericException(Exception ex, WebRequest request) {

        // Create a map to hold the error details
        Map<String, String> details = new HashMap<>();
        details.put("type", "Internal Server Error");
        details.put("message", ex.getMessage());

        // Create a StandardResponse object with the error details
        StandardResponse response = new StandardResponse("error", null, details);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}