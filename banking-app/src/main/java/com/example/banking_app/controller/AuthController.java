package com.example.banking_app.controller;

import com.example.banking_app.dto.JwtResponseDto;
import com.example.banking_app.dto.LoginDto;
import com.example.banking_app.dto.RegisterDto;
import com.example.banking_app.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterDto registerDto){

        return ResponseEntity.ok(
                authService.register(registerDto)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(
            @RequestBody LoginDto loginDto){

        return ResponseEntity.ok(
                authService.login(loginDto)
        );
    }
}