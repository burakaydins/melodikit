package com.example.playlistmanager.repository;

import com.example.playlistmanager.model.ListenHistory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListenHistoryRepository extends JpaRepository<ListenHistory, Long> {
    default List<ListenHistory> findLatest(int limit) {
        return findAll(Sort.by(Sort.Direction.DESC, "listenedAt"))
                .stream()
                .limit(limit)
                .toList();
    }
}
