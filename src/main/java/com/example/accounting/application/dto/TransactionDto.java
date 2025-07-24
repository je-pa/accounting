package com.example.accounting.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record TransactionDto(
    LocalDateTime transactionDateTime,
    String description,
    BigDecimal amount,
    String categoryId,
    String categoryName,
    String companyName
) {}


