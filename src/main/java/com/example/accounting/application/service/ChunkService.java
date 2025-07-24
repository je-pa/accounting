package com.example.accounting.application.service;

import com.example.accounting.application.dto.BankTransactionCsvRowDto;
import com.example.accounting.domain.entity.ChunkHistory;
import com.example.accounting.domain.repository.ChunkHistoryRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChunkService {

  private final ChunkHistoryRepository chunkHistoryRepository;

  public ChunkHistory saveChunkHistory(List<BankTransactionCsvRowDto> chunk) {
    ChunkHistory history = ChunkHistory.builder()
        .chunkIndex(0)
        .size(chunk.size())
        .startedAt(LocalDateTime.now())
        .build();
    return chunkHistoryRepository.save(history);
  }

  public void markSuccess(ChunkHistory history) {
    history.markSuccess(LocalDateTime.now());
    chunkHistoryRepository.save(history);
  }

  public void markFailed(ChunkHistory history, String reason) {
    history.markFailed(reason, LocalDateTime.now());
    chunkHistoryRepository.save(history);
  }
}
