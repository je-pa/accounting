package com.example.accounting.infrastructure.persistence;

import com.example.accounting.domain.entity.Category;
import com.example.accounting.domain.repository.CategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCategoryRepository extends JpaRepository<Category, String>, CategoryRepository {

}
