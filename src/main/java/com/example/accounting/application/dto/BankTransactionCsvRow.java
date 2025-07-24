package com.example.accounting.application.dto;

import com.opencsv.bean.CsvBindByName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
public class BankTransactionCsvRow {
  @CsvBindByName(column = "거래일시", required = true)
  private String transactionDateTimeRaw;

  @CsvBindByName(column = "적요")
  private String description;

  @CsvBindByName(column = "입금액")
  private BigDecimal depositAmount;

  @CsvBindByName(column = "출금액")
  private BigDecimal withdrawalAmount;

  @CsvBindByName(column = "거래후잔액")
  private BigDecimal balanceAfter;

  @CsvBindByName(column = "거래점")
  private String branch;

  public LocalDateTime transactionDateTime() {
    return LocalDateTime.parse(transactionDateTimeRaw, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

}


