package com.example.accounting.infrastructure.persistence;

import com.example.accounting.domain.entity.QCategory;
import com.example.accounting.domain.entity.QCompany;
import com.example.accounting.domain.entity.QTransaction;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryCustomImpl implements TransactionRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<TransactionRecordDto> findTransactionRecordsByCompanyId(String companyId) {
    QTransaction tx = QTransaction.transaction;
    QCategory category = QCategory.category;
    QCompany company = QCompany.company;

    return queryFactory
        .select(new QTransactionRecordDto(
            tx.transactionDateTime,
            tx.description,
            tx.depositAmount.subtract(tx.withdrawalAmount),
            tx.categoryId,
            category.name,
            company.name
        ))
        .from(tx)
        .leftJoin(category).on(tx.categoryId.eq(category.id))
        .leftJoin(company).on(tx.companyId.eq(company.id))
        .where(tx.companyId.eq(companyId))
        .fetch();
  }
}

