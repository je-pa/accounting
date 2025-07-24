package com.example.accounting.application;

import com.example.accounting.application.dto.BankTransactionCsvRow;
import com.example.accounting.application.dto.ClassificationResult;
import com.example.accounting.application.dto.RulesJsonDto;
import com.example.accounting.domain.entity.Category;
import com.example.accounting.domain.entity.Company;
import com.example.accounting.domain.entity.Transaction;
import com.example.accounting.domain.repository.CategoryRepository;
import com.example.accounting.domain.repository.CompanyRepository;
import com.example.accounting.domain.repository.TransactionRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountingService {

  private final TransactionRepository transactionRepository;
  private final CompanyRepository companyRepository;
  private final CategoryRepository categoryRepository;

  public List<ClassificationResult> classifyAndSave(
      List<BankTransactionCsvRow> transactions,
      RulesJsonDto rules
  ) {
    // ✅ 우선 JSON에 있는 Company/Category 를 DB에 등록 (없을 경우)
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
              newCategory.setCompany(company); // company 연관관계 설정
              return categoryRepository.save(newCategory);
            });
      }
    }

    // ✅ 기존 분류 및 저장 로직
    List<ClassificationResult> results = new ArrayList<>();

    for (BankTransactionCsvRow tx : transactions) {
      boolean matched = false;

      for (RulesJsonDto.CompanyRule company : rules.companies()) {
        for (RulesJsonDto.CategoryRule category : company.categories()) {
          for (String keyword : category.keywords()) {
            if (tx.getDescription().contains(keyword)) {
              Company c = companyRepository.findById(company.companyId()).orElseThrow();
              Category cat = categoryRepository.findById(category.categoryId()).orElseThrow();

              Transaction entity = new Transaction();
              entity.setTransactionDateTime(tx.transactionDateTime());
              entity.setDescription(tx.getDescription());
              entity.setDepositAmount(tx.getDepositAmount());
              entity.setWithdrawalAmount(tx.getWithdrawalAmount());
              entity.setBalanceAfter(tx.getBalanceAfter());
              entity.setBranch(tx.getBranch());
              entity.setCompany(c);
              entity.setCategory(cat);
              transactionRepository.save(entity);

              results.add(new ClassificationResult(tx, c.getId(), cat.getId(), cat.getName()));
              matched = true;
              break;
            }
          }
          if (matched) break;
        }
        if (matched) break;
      }

      if (!matched) {
        Transaction entity = new Transaction();
        entity.setTransactionDateTime(tx.transactionDateTime());
        entity.setDescription(tx.getDescription());
        entity.setDepositAmount(tx.getDepositAmount());
        entity.setWithdrawalAmount(tx.getWithdrawalAmount());
        entity.setBalanceAfter(tx.getBalanceAfter());
        entity.setBranch(tx.getBranch());
        transactionRepository.save(entity);

        results.add(new ClassificationResult(tx, null, null, "미분류"));
      }
    }

    return results;
  }

}

