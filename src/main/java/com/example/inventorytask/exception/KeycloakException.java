package com.example.inventorytask.exception;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class KeycloakException extends RuntimeException {
    private int errorCode;
    private String errorMessage;
}
