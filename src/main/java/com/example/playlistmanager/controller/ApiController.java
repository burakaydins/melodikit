package com.example.playlistmanager.controller;

import com.example.playlistmanager.model.Album;
import com.example.playlistmanager.model.Artist;
import com.example.playlistmanager.model.Playlist;
import com.example.playlistmanager.model.Song;
import com.example.playlistmanager.service.AlbumService;
import com.example.playlistmanager.service.ArtistService;
import com.example.playlistmanager.service.PlaylistService;
import com.example.playlistmanager.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private SongService songService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/songs")
    public List<Song> songs() {
        return songService.getAllSongs();
    }

    @GetMapping("/songs/{id}")
    public Song song(@PathVariable Long id) {
        return songService.getSongById(id);
    }

    @GetMapping("/artists")
    public List<Artist> artists() {
        return artistService.getAllArtists();
    }

    @GetMapping("/albums")
    public List<Album> albums() {
        return albumService.getAllAlbums();
    }

    @GetMapping("/playlists")
    public List<Playlist> playlists() {
        return playlistService.getAllPlaylists();
    }
}
