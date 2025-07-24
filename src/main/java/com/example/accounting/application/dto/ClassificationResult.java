package com.example.accounting.application.dto;

public record ClassificationResult(
    BankTransactionCsvRow transaction,
    String companyId,
    String categoryId,
    String categoryName
) {}
