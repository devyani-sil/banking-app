package com.example.banking_app.dto;

import lombok.Data;

@Data
public class TransferDto {
    private Long fromAccountId;
    private Long toAccountId;
    private Double amount;
}
