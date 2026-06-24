package com.example.banking_app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransferDto {

    @NotNull(message = "Sender account id is required")
    private Long fromAccountId;
    @NotNull(message = "Receiver account id is required")
    private Long toAccountId;
    @Positive(message = "Transfer amount must be greater than zero")
    private Double amount;
}
