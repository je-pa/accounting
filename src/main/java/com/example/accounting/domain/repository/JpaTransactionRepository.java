package com.example.accounting.domain.repository;

import com.example.accounting.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTransactionRepository extends JpaRepository<Transaction, Long> {

}
