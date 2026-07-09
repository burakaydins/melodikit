package com.example.playlistmanager.repository;

import com.example.playlistmanager.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
