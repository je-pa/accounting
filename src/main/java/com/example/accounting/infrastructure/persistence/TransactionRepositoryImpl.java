package com.example.accounting.infrastructure.persistence;

import com.example.accounting.application.dto.TransactionDto;
import com.example.accounting.domain.entity.Transaction;
import com.example.accounting.domain.repository.JpaTransactionRepository;
import com.example.accounting.domain.repository.TransactionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TransactionRepositoryImpl implements TransactionRepository {
  private final JpaTransactionRepository jpaTransactionRepository;
  private final TransactionRepositoryCustom transactionRepositoryCustom;

  @Override
  public <S extends Transaction> Iterable<S> saveAll(Iterable<S> entities) {
    return jpaTransactionRepository.saveAll(entities);
  }

  @Override
  public List<TransactionDto> findByCompanyId(String companyId) {
    return transactionRepositoryCustom.findTransactionRecordsByCompanyId(companyId)
        .stream().map(r -> r.toDto()).toList();
  }
}
