package com.example.banking_app.service.impl;

import com.example.banking_app.dto.JwtResponseDto;
import com.example.banking_app.dto.LoginDto;
import com.example.banking_app.dto.RegisterDto;
import com.example.banking_app.entity.Role;
import com.example.banking_app.entity.RoleName;
import com.example.banking_app.entity.User;
import com.example.banking_app.exception.ResourceNotFoundException;
import com.example.banking_app.repository.RoleRepository;
import com.example.banking_app.repository.UserRepository;
import com.example.banking_app.security.JwtTokenProvider;
import com.example.banking_app.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    private JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String register(RegisterDto registerDto) {

        // Check email already exists
        if(userRepository.existsByEmail(registerDto.getEmail())){

            throw new ResourceNotFoundException("Email already exists");
        }

        User user = new User();

        user.setName(registerDto.getName());

        user.setEmail(registerDto.getEmail());

        // Encrypt password
        user.setPassword(
                passwordEncoder.encode(registerDto.getPassword())
        );

        // Default role = USER
        Role role = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found"));

        user.setRoles(Set.of(role));

        userRepository.save(user);

        return "User registered successfully";
    }

    @Override
    public JwtResponseDto login(LoginDto loginDto) {

        User user = userRepository.findByEmail(
                loginDto.getEmail()
        ).orElseThrow(() ->
                new RuntimeException("Invalid credentials"));

        // Compare raw password with BCrypt password
        if(!passwordEncoder.matches(
                loginDto.getPassword(),
                user.getPassword())){

            throw new ResourceNotFoundException("Invalid credentials");
        }

        List<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .toList();

        String token = jwtTokenProvider.generateToken(
                user.getEmail(),
                roles
        );

        return new JwtResponseDto(
                token,
                "Bearer"
        );
    }

    // register() and login() methods will go here
}