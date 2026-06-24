package com.example.banking_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDto {
    private Long id;
    @NotBlank(message = "Account holder name cannot be empty")
    private String accountHolderName;
    @PositiveOrZero(message = "Balance cannot be negative")
    private double balance;
}
