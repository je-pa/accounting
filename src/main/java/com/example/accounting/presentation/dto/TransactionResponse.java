package com.example.accounting.presentation.dto;

import com.example.accounting.application.dto.TransactionDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
    LocalDateTime transactionDateTime,
    String description,
    BigDecimal amount,
    String categoryId,
    String categoryName,
    String companyName
) {
  public static TransactionResponse from(TransactionDto dto) {
    return new TransactionResponse(
        dto.transactionDateTime(),
        dto.description(),
        dto.amount(),
        dto.categoryId(),
        dto.categoryName(),
        dto.companyName()
    );
  }
}
