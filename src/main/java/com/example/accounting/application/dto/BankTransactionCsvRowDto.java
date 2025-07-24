package com.example.accounting.application.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BankTransactionCsvRowDto(
    String transactionDateTimeRaw,
    String description,
    BigDecimal depositAmount,
    BigDecimal withdrawalAmount,
    BigDecimal balanceAfter,
    String branch,
    LocalDateTime transactionDateTime
) {}


