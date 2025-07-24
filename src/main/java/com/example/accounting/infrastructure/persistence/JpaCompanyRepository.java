package com.example.accounting.infrastructure.persistence;

import com.example.accounting.domain.entity.Company;
import com.example.accounting.domain.repository.CompanyRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCompanyRepository extends JpaRepository<Company, String>, CompanyRepository {

}
