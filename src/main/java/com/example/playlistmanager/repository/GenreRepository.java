package com.example.playlistmanager.repository;

import com.example.playlistmanager.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}