package com.example.accounting.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Category {
  @Id
  private String id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;
}

