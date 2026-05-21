package com.example.banking_app.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDto {

    private Long accountId;

    private Double amount;

    private String type;

    private String status;

    private LocalDateTime timestamp;
}