package com.example.accounting.application.service;

import com.example.accounting.application.dto.RulesJsonDto;
import com.example.accounting.domain.entity.Category;
import com.example.accounting.domain.entity.Company;
import com.example.accounting.domain.repository.CategoryRepository;
import com.example.accounting.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RuleService {

  private final CompanyRepository companyRepository;
  private final CategoryRepository categoryRepository;

  public void saveRules(RulesJsonDto rules) {
    for (RulesJsonDto.CompanyRule companyRule : rules.companies()) {
      Company company = companyRepository.findById(companyRule.companyId())
          .orElseGet(() -> {
            Company newCompany = new Company();
            newCompany.setId(companyRule.companyId());
            newCompany.setName(companyRule.companyName());
            return companyRepository.save(newCompany);
          });

      for (RulesJsonDto.CategoryRule categoryRule : companyRule.categories()) {
        categoryRepository.findById(categoryRule.categoryId())
            .orElseGet(() -> {
              Category newCategory = new Category();
              newCategory.setId(categoryRule.categoryId());
              newCategory.setName(categoryRule.categoryName());
              newCategory.setCompany(company);
              return categoryRepository.save(newCategory);
            });
      }
    }
  }
}

