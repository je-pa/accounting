package com.example.accounting.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private LocalDateTime transactionDateTime;
  private String description;
  private BigDecimal depositAmount;
  private BigDecimal withdrawalAmount;
  private BigDecimal balanceAfter;
  private String branch;

  private String companyId;
  private String categoryId;

  @Builder
  public Transaction(LocalDateTime transactionDateTime,
      String description,
      BigDecimal depositAmount,
      BigDecimal withdrawalAmount,
      BigDecimal balanceAfter,
      String branch,
      String companyId,
      String categoryId) {
    this.transactionDateTime = transactionDateTime;
    this.description = description;
    this.depositAmount = depositAmount;
    this.withdrawalAmount = withdrawalAmount;
    this.balanceAfter = balanceAfter;
    this.branch = branch;
    this.companyId = companyId;
    this.categoryId = categoryId;
  }
}


