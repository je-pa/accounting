package com.example.accounting.domain.repository;

import com.example.accounting.domain.entity.Category;
import java.util.Optional;

public interface CategoryRepository {

  Optional<Category> findById(String s);

  Category save(Category newCategory);
}
