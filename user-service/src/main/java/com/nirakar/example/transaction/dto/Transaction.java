package com.nirakar.example.transaction.dto;

import java.time.LocalDate;

public record Transaction(
    String transactionId,
    LocalDate date,
    String description,
    String type,
    double amount,
    double balance
) {

}
