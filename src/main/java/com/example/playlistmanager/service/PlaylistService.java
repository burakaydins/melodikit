package com.example.playlistmanager.service;

import com.example.playlistmanager.model.Playlist;
import com.example.playlistmanager.repository.PlaylistRepository;
import com.example.playlistmanager.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    public Playlist getPlaylistById(Long id) {
        return playlistRepository.findById(id).orElse(null);
    }

    public void savePlaylist(Playlist playlist) {
        playlistRepository.save(playlist);
    }

    public void savePlaylist(Playlist playlist, List<Long> songIds) {
        if (playlist.getName() == null || playlist.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Playlist adı boş bırakılamaz.");
        }
        playlist.setName(playlist.getName().trim());
        if (songIds != null) {
            playlist.setSongs(songRepository.findAllById(songIds));
        }
        playlistRepository.save(playlist);
    }

    public void deletePlaylist(Long id) {
        playlistRepository.deleteById(id);
    }

    public void addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = getPlaylistById(playlistId);
        if (playlist == null || songId == null) {
            return;
        }
        List<com.example.playlistmanager.model.Song> songs = playlist.getSongs();
        if (songs == null) {
            songs = new java.util.ArrayList<>();
        }
        com.example.playlistmanager.model.Song song = songRepository.findById(songId).orElse(null);
        if (song != null && songs.stream().noneMatch(existing -> existing.getId().equals(songId))) {
            songs.add(song);
            playlist.setSongs(songs);
            playlistRepository.save(playlist);
        }
    }

    public void removeSongFromPlaylist(Long playlistId, Long songId) {
        Playlist playlist = getPlaylistById(playlistId);
        if (playlist == null || playlist.getSongs() == null || songId == null) {
            return;
        }
        playlist.getSongs().removeIf(song -> song.getId().equals(songId));
        playlistRepository.save(playlist);
    }

    public int getTotalDuration(Playlist playlist) {
        if (playlist == null || playlist.getSongs() == null) {
            return 0;
        }
        return playlist.getSongs().stream()
                .mapToInt(song -> Math.max(song.getDuration(), 0))
                .sum();
    }
}
