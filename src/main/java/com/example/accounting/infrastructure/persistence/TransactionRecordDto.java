package com.example.accounting.infrastructure.persistence;

import com.example.accounting.application.dto.TransactionDto;
import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionRecordDto(
    LocalDateTime transactionDateTime,
    String description,
    BigDecimal amount,
    String categoryId,
    String categoryName,
    String companyName
) {
  @QueryProjection
  public TransactionRecordDto {
  }

  public TransactionDto toDto() {
    return TransactionDto.builder()
        .transactionDateTime(transactionDateTime)
        .description(description)
        .amount(amount)
        .categoryId(categoryId)
        .categoryName(categoryName)
        .companyName(companyName)
        .build();
  }
}

