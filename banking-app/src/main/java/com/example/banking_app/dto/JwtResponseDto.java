package com.example.banking_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";
}
