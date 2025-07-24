package com.example.accounting.application.service;

import com.example.accounting.application.dto.BankTransactionCsvRowDto;
import com.example.accounting.application.dto.ClassificationResult;
import com.example.accounting.application.dto.RulesJsonDto;
import com.example.accounting.application.dto.TransactionDto;
import com.example.accounting.domain.entity.Transaction;
import com.example.accounting.domain.repository.TransactionRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountingService {

  private final TransactionRepository transactionRepository;

  public List<ClassificationResult> classifyAndSave(
      List<BankTransactionCsvRowDto> chunk,
      RulesJsonDto rules
  ) {
    List<Transaction> transactionsToSave = new ArrayList<>();
    List<ClassificationResult> results = new ArrayList<>();

    for (BankTransactionCsvRowDto tx : chunk) {
      MatchingResult match = findMatchingRule(tx.description(), rules);

      Transaction transaction = Transaction.builder()
          .transactionDateTime(tx.transactionDateTime())
          .description(tx.description())
          .depositAmount(tx.depositAmount())
          .withdrawalAmount(tx.withdrawalAmount())
          .balanceAfter(tx.balanceAfter())
          .branch(tx.branch())
          .companyId(match.companyId())
          .categoryId(match.categoryId())
          .build();

      transactionsToSave.add(transaction);

      results.add(new ClassificationResult(
          tx,
          match.companyId(),
          match.categoryId()
      ));
    }

    transactionRepository.saveAll(transactionsToSave);

    return results;
  }

  private MatchingResult findMatchingRule(String description, RulesJsonDto rules) {
    if (description == null) return MatchingResult.empty();

    for (RulesJsonDto.CompanyRule company : rules.companies()) {
      for (RulesJsonDto.CategoryRule category : company.categories()) {
        boolean matched = category.keywords().stream().anyMatch(description::contains);
        if (matched) {
          return new MatchingResult(
              company.companyId(),
              category.categoryId()
          );
        }
      }
    }

    return MatchingResult.empty();
  }

  public List<TransactionDto> getTransactionsByCompanyId(String companyId) {
    return transactionRepository.findByCompanyId(companyId);
  }

  private record MatchingResult(
      String companyId,
      String categoryId
  ) {
    static MatchingResult empty() {
      return new MatchingResult(null, null);
    }
  }

}


