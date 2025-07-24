package com.example.accounting.infrastructure.persistence;

import com.example.accounting.domain.entity.ChunkHistory;
import com.example.accounting.domain.repository.ChunkHistoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChunkHistoryRepository extends JpaRepository<ChunkHistory, Long>,
    ChunkHistoryRepository {

}
