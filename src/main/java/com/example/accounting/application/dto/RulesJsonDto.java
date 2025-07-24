package com.example.accounting.application.dto;

import java.util.List;

public record RulesJsonDto(
    List<CompanyRule> companies
) {
  public record CompanyRule(
      String companyId,
      String companyName,
      List<CategoryRule> categories
  ) {}

  public record CategoryRule(
      String categoryId,
      String categoryName,
      List<String> keywords
  ) {}
}
