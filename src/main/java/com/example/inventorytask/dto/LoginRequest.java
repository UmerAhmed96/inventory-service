package com.example.inventorytask.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;
}
