package com.example.accounting.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChunkHistory {

  @Id
  @GeneratedValue
  private Long id;

  private int chunkIndex;
  private int size;
  private LocalDateTime startedAt;
  private LocalDateTime finishedAt;
  private boolean success;
  private String errorMessage;

  public void markSuccess(LocalDateTime finishedAt) {
    this.success = true;
    this.finishedAt = finishedAt;
  }

  public void markFailed(String errorMessage, LocalDateTime finishedAt) {
    this.success = false;
    this.errorMessage = errorMessage;
    this.finishedAt = finishedAt;
  }
}

