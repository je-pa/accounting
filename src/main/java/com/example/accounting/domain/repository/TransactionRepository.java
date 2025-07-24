package com.example.accounting.domain.repository;

import com.example.accounting.domain.entity.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  List<Transaction> findByCompany_Id(String companyId);
}
