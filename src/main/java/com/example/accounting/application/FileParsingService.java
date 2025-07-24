package com.example.accounting.application;

import com.example.accounting.application.dto.BankTransactionCsvRow;
import com.example.accounting.application.dto.RulesJsonDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileParsingService {

  public List<BankTransactionCsvRow> parseCsv(MultipartFile csvFile) throws IOException {
    try (Reader reader = new InputStreamReader(csvFile.getInputStream())) {
      return new CsvToBeanBuilder<BankTransactionCsvRow>(reader)
          .withType(BankTransactionCsvRow.class)
          .withIgnoreLeadingWhiteSpace(true)
          .build()
          .parse();
    }
  }

  public RulesJsonDto parseRules(MultipartFile jsonFile) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    return objectMapper.readValue(jsonFile.getInputStream(), RulesJsonDto.class);
  }
}

