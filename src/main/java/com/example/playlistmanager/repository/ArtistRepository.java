package com.example.playlistmanager.repository;

import com.example.playlistmanager.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
