package com.example.inventorytask.exception.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class ApiError {
    private HttpStatus status;
    private String message;

    // Getters and setters
}
