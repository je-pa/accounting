package com.example.accounting.presentation;

import com.example.accounting.application.AccountingService;
import com.example.accounting.application.FileParsingService;
import com.example.accounting.application.dto.BankTransactionCsvRow;
import com.example.accounting.application.dto.ClassificationResult;
import com.example.accounting.application.dto.RulesJsonDto;
import com.example.accounting.domain.entity.Transaction;
import com.example.accounting.domain.repository.TransactionRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/accounting")
@RequiredArgsConstructor
public class AccountingController {

  private final FileParsingService fileParsingService;
  private final AccountingService accountingService;
  private final TransactionRepository transactionRepository;

  @PostMapping(value = "/process")
  public ResponseEntity<?> processFiles(
      @RequestPart MultipartFile csvFile,
      @RequestPart MultipartFile rulesFile
  ) throws IOException {

    List<BankTransactionCsvRow> transactions = fileParsingService.parseCsv(csvFile);
    RulesJsonDto rules = fileParsingService.parseRules(rulesFile);

    List<ClassificationResult> results = accountingService.classifyAndSave(transactions, rules);

    long unclassifiedCount = results.stream().filter(r -> r.companyId() == null).count();

    return ResponseEntity.ok(Map.of(
        "processed", results.size(),
        "classified", results.size() - unclassifiedCount,
        "unclassified", unclassifiedCount
    ));
  }

  @GetMapping("/records")
  public ResponseEntity<?> getRecords(@RequestParam String companyId) {
    List<Transaction> txs = transactionRepository.findByCompany_Id(companyId);
    return ResponseEntity.ok(txs.stream().map(tx -> Map.of(
        "transactionDate", tx.getTransactionDateTime(),
        "description", tx.getDescription(),
        "amount", tx.getDepositAmount().subtract(tx.getWithdrawalAmount()),
        "category_id", tx.getCategory() != null ? tx.getCategory().getId() : null,
        "category_name", tx.getCategory() != null ? tx.getCategory().getName() : "미분류"
    )).toList());
  }
}

