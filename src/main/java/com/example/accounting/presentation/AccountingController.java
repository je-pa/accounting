package com.example.accounting.presentation;

import com.example.accounting.application.dto.ProcessResult;
import com.example.accounting.application.dto.TransactionDto;
import com.example.accounting.application.facade.AccountingFacade;
import com.example.accounting.infrastructure.persistence.TransactionRepositoryImpl;
import com.example.accounting.presentation.dto.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
  private final AccountingFacade accountingFacade;
  private final TransactionRepositoryImpl transactionRepository;

  @Operation(summary = "process", description = "Multipart 파일을 업로드합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청")
  })
  @PostMapping(value = "/process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ProcessResult> processFiles(
      @Parameter(description = "csvFile", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
      @RequestPart MultipartFile csvFile,
      @Parameter(description = "rulesFile", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
      @RequestPart MultipartFile rulesFile
  ) throws IOException {

    ProcessResult result = accountingFacade.process(csvFile, rulesFile);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/records")
  public ResponseEntity<List<TransactionResponse>> getRecords(@RequestParam String companyId) {
    List<TransactionDto> txs = accountingFacade.getTransactions(companyId);
    return ResponseEntity.ok(txs.stream()
        .map(TransactionResponse::from)
        .toList());
  }
}

