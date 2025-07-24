package com.example.accounting.application.dto;

public record ClassificationResult(
    BankTransactionCsvRowDto transaction,
    String companyId,
    String categoryId
) {}
