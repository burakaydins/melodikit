package com.example.playlistmanager.service;

import com.example.playlistmanager.model.ListenHistory;
import com.example.playlistmanager.model.Song;
import com.example.playlistmanager.repository.ListenHistoryRepository;
import com.example.playlistmanager.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ListenHistoryRepository listenHistoryRepository;

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public List<Song> getLatestSongs(int limit) {
        return songRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .limit(limit)
                .toList();
    }

    public List<Song> getTopRatedSongs(int limit) {
        return songRepository.findAll(Sort.by(Sort.Direction.DESC, "rating"))
                .stream()
                .limit(limit)
                .toList();
    }

    public List<Song> getMostPlayedSongs(int limit) {
        return songRepository.findAll(Sort.by(Sort.Direction.DESC, "playCount"))
                .stream()
                .limit(limit)
                .toList();
    }

    public List<Song> searchSongs(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllSongs();
        }
        String value = keyword.trim();
        return songRepository.findByTitleContainingIgnoreCaseOrArtistNameContainingIgnoreCaseOrAlbumTitleContainingIgnoreCaseOrGenreNameContainingIgnoreCase(
                value, value, value, value
        );
    }

    public List<Song> filterSongs(String keyword,
                                  Long artistId,
                                  Long albumId,
                                  Long genreId,
                                  Boolean favorite,
                                  Integer rating,
                                  String sort) {
        Stream<Song> stream = searchSongs(keyword).stream();

        if (artistId != null) {
            stream = stream.filter(song -> song.getArtist() != null && artistId.equals(song.getArtist().getId()));
        }
        if (albumId != null) {
            stream = stream.filter(song -> song.getAlbum() != null && albumId.equals(song.getAlbum().getId()));
        }
        if (genreId != null) {
            stream = stream.filter(song -> song.getGenre() != null && genreId.equals(song.getGenre().getId()));
        }
        if (Boolean.TRUE.equals(favorite)) {
            stream = stream.filter(Song::isFavorite);
        }
        if (rating != null) {
            stream = stream.filter(song -> song.getRating() >= rating);
        }

        Comparator<Song> comparator = switch (sort == null ? "" : sort) {
            case "rating" -> Comparator.comparingInt(Song::getRating).reversed();
            case "duration" -> Comparator.comparingInt(Song::getDuration).reversed();
            default -> Comparator.comparing(Song::getTitle, String.CASE_INSENSITIVE_ORDER);
        };

        return stream.sorted(comparator).toList();
    }

    public List<Song> getFavoriteSongs() {
        return songRepository.findByFavoriteTrue();
    }

    public Song getSongById(Long id) {
        return songRepository.findById(id).orElse(null);
    }

    public void saveSong(Song song) {
        if (song.getTitle() == null || song.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Şarkı adı boş bırakılamaz.");
        }
        song.setTitle(song.getTitle().trim());
        if (song.getRating() < 0) {
            song.setRating(0);
        }
        if (song.getRating() > 5) {
            song.setRating(5);
        }
        if (song.getArtist() != null && song.getArtist().getId() == null) {
            song.setArtist(null);
        }
        if (song.getAlbum() != null && song.getAlbum().getId() == null) {
            song.setAlbum(null);
        }
        if (song.getGenre() != null && song.getGenre().getId() == null) {
            song.setGenre(null);
        }
        if (song.getAudioUrl() != null) {
            song.setAudioUrl(song.getAudioUrl().trim());
        }
        Song savedSong = songRepository.save(song);
        if (savedSong.getAudioUrl() == null || savedSong.getAudioUrl().isBlank()) {
            savedSong.setAudioUrl("/songs/" + savedSong.getId() + "/preview");
            songRepository.save(savedSong);
        }
    }

    public void deleteSong(Long id) {
        songRepository.deleteById(id);
    }

    public void toggleFavorite(Long id) {
        Song song = getSongById(id);
        if (song != null) {
            song.setFavorite(!song.isFavorite());
            songRepository.save(song);
        }
    }

    public int increasePlayCount(Long id) {
        Song song = getSongById(id);
        if (song != null) {
            song.setPlayCount(song.getPlayCount() + 1);
            songRepository.save(song);

            ListenHistory history = new ListenHistory();
            history.setSong(song);
            history.setListenedAt(LocalDateTime.now());
            listenHistoryRepository.save(history);
            return song.getPlayCount();
        }
        return 0;
    }
}
