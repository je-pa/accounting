package com.example.accounting.domain.repository;

import com.example.accounting.domain.entity.Company;
import java.util.Optional;

public interface CompanyRepository {

  Optional<Company> findById(String s);

  Company save(Company newCompany);
}
