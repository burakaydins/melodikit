package com.example.playlistmanager.repository;

import com.example.playlistmanager.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByTitleContainingIgnoreCaseOrArtistNameContainingIgnoreCaseOrAlbumTitleContainingIgnoreCaseOrGenreNameContainingIgnoreCase(
            String title,
            String artistName,
            String albumTitle,
            String genreName
    );

    List<Song> findByFavoriteTrue();
}
