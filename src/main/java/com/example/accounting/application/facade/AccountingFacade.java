package com.example.accounting.application.facade;

import com.example.accounting.application.dto.BankTransactionCsvRowDto;
import com.example.accounting.application.dto.ClassificationResult;
import com.example.accounting.application.dto.ProcessResult;
import com.example.accounting.application.dto.RulesJsonDto;
import com.example.accounting.application.dto.TransactionDto;
import com.example.accounting.application.service.AccountingService;
import com.example.accounting.application.service.ChunkService;
import com.example.accounting.application.service.FileParsingService;
import com.example.accounting.application.service.RuleService;
import com.example.accounting.domain.entity.ChunkHistory;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AccountingFacade {

  private static final int CHUNK_SIZE = 100;

  private final FileParsingService fileParsingService;
  private final RuleService ruleService;
  private final ChunkService chunkService;
  private final AccountingService accountingService;

  public ProcessResult process(MultipartFile csvFile, MultipartFile rulesFile) throws IOException {
    // 1. JSON 처리 및 저장
    RulesJsonDto rules = fileParsingService.parseRules(rulesFile);
    ruleService.saveRules(rules);

    // 2. CSV 처리
    List<BankTransactionCsvRowDto> transactions = fileParsingService.parseCsv(csvFile);

    int total = transactions.size();
    int classified = 0;
    int unclassified = 0;

    // 3. Chunk 단위 처리
    for (int i = 0; i < transactions.size(); i += CHUNK_SIZE) {
      int end = Math.min(i + CHUNK_SIZE, transactions.size());
      List<BankTransactionCsvRowDto> chunk = transactions.subList(i, end);

      // 3-1. 이력 저장
      ChunkHistory history = chunkService.saveChunkHistory(chunk);

      try {
        List<ClassificationResult> results = accountingService.classifyAndSave(chunk, rules);

        classified += results.stream().filter(r -> r.companyId() != null).count();
        unclassified += results.stream().filter(r -> r.companyId() == null).count();

        chunkService.markSuccess(history);
      } catch (Exception e) {
        chunkService.markFailed(history, e.getMessage());
      }
    }

    return ProcessResult.builder().total(total).classified(classified).unclassified(unclassified).build();
  }

  public List<TransactionDto> getTransactions(String companyId) {
    return accountingService.getTransactionsByCompanyId(companyId);
  }
}

