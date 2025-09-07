package com.cars.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthDTO {
    
    private String token;
    private String type = "Bearer";
    private String message;
    private Long userId;
    private String username;
    private String email;
    
    public AuthDTO(String token, String message, Long userId, String username, String email) {
        this.token = token;
        this.message = message;
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
}

