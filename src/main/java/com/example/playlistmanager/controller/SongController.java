package com.example.playlistmanager.controller;

import com.example.playlistmanager.model.Song;
import com.example.playlistmanager.service.AlbumService;
import com.example.playlistmanager.service.ArtistService;
import com.example.playlistmanager.service.GenreService;
import com.example.playlistmanager.service.PlaylistService;
import com.example.playlistmanager.service.PreviewAudioService;
import com.example.playlistmanager.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Controller
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService songService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private PreviewAudioService previewAudioService;

    @GetMapping
    public String listSongs(@RequestParam(required = false) String keyword,
                            @RequestParam(required = false) Long artistId,
                            @RequestParam(required = false) Long albumId,
                            @RequestParam(required = false) Long genreId,
                            @RequestParam(required = false) Boolean favorites,
                            @RequestParam(required = false) Integer rating,
                            @RequestParam(required = false) String sort,
                            Model model) {
        model.addAttribute("songs", songService.filterSongs(keyword, artistId, albumId, genreId, favorites, rating, sort));
        model.addAttribute("keyword", keyword);
        model.addAttribute("artistId", artistId);
        model.addAttribute("albumId", albumId);
        model.addAttribute("genreId", genreId);
        model.addAttribute("favorites", Boolean.TRUE.equals(favorites));
        model.addAttribute("rating", rating);
        model.addAttribute("sort", sort);
        model.addAttribute("artists", artistService.getAllArtists());
        model.addAttribute("albums", albumService.getAllAlbums());
        model.addAttribute("genres", genreService.getAllGenres());
        return "songs/list";
    }

    @GetMapping("/new")
    public String newSongForm(Model model) {
        model.addAttribute("song", new Song());
        model.addAttribute("genres", genreService.getAllGenres());
        model.addAttribute("artists", artistService.getAllArtists());
        model.addAttribute("albums", albumService.getAllAlbums());
        return "songs/form";
    }

    @GetMapping("/{id}")
    public String songDetail(@PathVariable Long id, Model model) {
        model.addAttribute("song", songService.getSongById(id));
        model.addAttribute("playlists", playlistService.getAllPlaylists());
        return "songs/detail";
    }

    @GetMapping("/favorites")
    public String favoriteSongs(Model model) {
        model.addAttribute("songs", songService.getFavoriteSongs());
        return "songs/favorites";
    }

    @GetMapping("/export.csv")
    public ResponseEntity<byte[]> exportSongsCsv() {
        StringBuilder csv = new StringBuilder("Sarki,Sanatci,Album,Tur,Sure,Puan,Dinlenme,Favori\n");
        for (Song song : songService.getAllSongs()) {
            csv.append(csvValue(song.getTitle())).append(",");
            csv.append(csvValue(song.getArtist() != null ? song.getArtist().getName() : "")).append(",");
            csv.append(csvValue(song.getAlbum() != null ? song.getAlbum().getTitle() : "")).append(",");
            csv.append(csvValue(song.getGenre() != null ? song.getGenre().getName() : "")).append(",");
            csv.append(song.getDuration()).append(",");
            csv.append(song.getRating()).append(",");
            csv.append(song.getPlayCount()).append(",");
            csv.append(song.isFavorite() ? "Evet" : "Hayir").append("\n");
        }

        byte[] content = csv.toString().getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=melodikit-sarkilar.csv")
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(content);
    }

    @PostMapping("/save")
    public String saveSong(@ModelAttribute Song song, Model model) {
        try {
            songService.saveSong(song);
        } catch (IllegalArgumentException exception) {
            model.addAttribute("error", exception.getMessage());
            model.addAttribute("song", song);
            model.addAttribute("genres", genreService.getAllGenres());
            model.addAttribute("artists", artistService.getAllArtists());
            model.addAttribute("albums", albumService.getAllAlbums());
            return "songs/form";
        }
        return "redirect:/songs";
    }

    @GetMapping("/edit/{id}")
    public String editSong(@PathVariable Long id, Model model) {
        model.addAttribute("song", songService.getSongById(id));
        model.addAttribute("genres", genreService.getAllGenres());
        model.addAttribute("artists", artistService.getAllArtists());
        model.addAttribute("albums", albumService.getAllAlbums());
        return "songs/form";
    }

    @GetMapping("/favorite/{id}")
    public String toggleFavorite(@PathVariable Long id) {
        songService.toggleFavorite(id);
        return "redirect:/songs";
    }

    @PostMapping("/{id}/add-to-playlist")
    public String addToPlaylist(@PathVariable Long id, @RequestParam Long playlistId) {
        playlistService.addSongToPlaylist(playlistId, id);
        return "redirect:/songs/" + id;
    }

    @PostMapping("/{id}/play")
    @ResponseBody
    public Map<String, Integer> increasePlayCount(@PathVariable Long id) {
        return Map.of("playCount", songService.increasePlayCount(id));
    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<Void> previewAudio(@PathVariable Long id) {
        Song song = songService.getSongById(id);
        String previewUrl = previewAudioService.findPreviewUrl(song)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Preview bulunamadı."));

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(previewUrl))
                .build();
    }

    @GetMapping("/delete/{id}")
    public String deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return "redirect:/songs";
    }

    private String csvValue(String value) {
        if (value == null) {
            return "\"\"";
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
