package com.example.playlistmanager.repository;

import com.example.playlistmanager.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}
