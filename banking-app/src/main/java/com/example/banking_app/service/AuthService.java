package com.example.banking_app.service;

import com.example.banking_app.dto.JwtResponseDto;
import com.example.banking_app.dto.LoginDto;
import com.example.banking_app.dto.RegisterDto;

public interface AuthService {

    String register(RegisterDto registerDto);

    JwtResponseDto login(LoginDto loginDto);
}
