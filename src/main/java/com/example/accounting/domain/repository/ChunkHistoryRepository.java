package com.example.accounting.domain.repository;

import com.example.accounting.domain.entity.ChunkHistory;

public interface ChunkHistoryRepository {

  ChunkHistory save(ChunkHistory history);
}
