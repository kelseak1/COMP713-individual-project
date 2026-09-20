package nz.ac.aut.community_centre_booking.exception;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.aut.community_centre_booking.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

// converts app exceptions into consistent HTTP responses
@RestControllerAdvice
public class GlobalErrorHandler {

    // returns the first validation error message for invalid requests
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("The request is invalid.");

        ApiError error = new ApiError(
                "INVALID_REQUEST",
                message,
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    // returns a 404 response for resources that cannot be found
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> handleNotFound(
            NoSuchElementException exception,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                "RESOURCE_NOT_FOUND",
                exception.getMessage(),
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    // returns a 400 response for invalid requests, such as invalid room IDs
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleInvalidRequest(
            IllegalArgumentException exception,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                "INVALID_REQUEST",
                exception.getMessage(),
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    // returns a 409 response for booking conflicts, such as overlapping bookings
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleConflict(
            IllegalStateException exception,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                "BOOKING_CONFLICT",
                exception.getMessage(),
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
}