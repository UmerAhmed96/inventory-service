package com.example.inventorytask.interceptor;

import com.example.inventorytask.exception.ItemAlreadyExistsException;
import com.example.inventorytask.exception.ItemNotFoundException;
import com.example.inventorytask.exception.error.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Handles ItemNotFoundException
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<Object> handleItemNotFoundException(ItemNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    // Handles ItemAlreadyExistsException
    @ExceptionHandler(ItemAlreadyExistsException.class)
    public ResponseEntity<Object> handleItemAlreadyExistsException(ItemAlreadyExistsException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    // Handles ConstraintViolationException
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        // Get the error message from the exception
        String errorMessage = ex.getMessage();

        // Return a custom response with the error message
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
