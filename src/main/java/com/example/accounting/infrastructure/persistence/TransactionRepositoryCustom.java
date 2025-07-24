package com.example.accounting.infrastructure.persistence;

import java.util.List;

public interface TransactionRepositoryCustom {
  List<TransactionRecordDto> findTransactionRecordsByCompanyId(String companyId);
}

