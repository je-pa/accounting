package com.example.accounting.application.service;

import com.example.accounting.application.dto.BankTransactionCsvRowDto;
import com.example.accounting.application.dto.RulesJsonDto;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FileParsingService {

  List<BankTransactionCsvRowDto> parseCsv(MultipartFile csvFile) throws IOException;

  RulesJsonDto parseRules(MultipartFile jsonFile) throws IOException;
}

