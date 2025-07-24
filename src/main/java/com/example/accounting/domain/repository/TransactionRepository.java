package com.example.accounting.domain.repository;

import com.example.accounting.application.dto.TransactionDto;
import com.example.accounting.domain.entity.Transaction;
import java.util.List;

public interface TransactionRepository {

  <S extends Transaction> Iterable<S> saveAll(Iterable<S> entities);

  List<TransactionDto> findByCompanyId(String companyId);
}
