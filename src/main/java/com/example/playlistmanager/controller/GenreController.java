package com.example.playlistmanager.controller;

import com.example.playlistmanager.model.Genre;
import com.example.playlistmanager.model.Song;
import com.example.playlistmanager.service.GenreService;
import com.example.playlistmanager.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @Autowired
    private SongService songService;

    @GetMapping
    public String listGenres(Model model) {
        model.addAttribute("genres", genreService.getAllGenres());
        return "genres/list";
    }

    @GetMapping("/new")
    public String newGenreForm(Model model) {
        model.addAttribute("genre", new Genre());
        return "genres/form";
    }

    @GetMapping("/{id}")
    public String genreDetail(@PathVariable Long id, Model model) {
        Genre genre = genreService.getGenreById(id);
        List<Song> songs = songService.filterSongs(null, null, null, id, null, null, "title");
        int totalDuration = songs.stream()
                .mapToInt(song -> Math.max(song.getDuration(), 0))
                .sum();

        model.addAttribute("genre", genre);
        model.addAttribute("songs", songs);
        model.addAttribute("totalDuration", totalDuration);
        return "genres/detail";
    }

    @PostMapping("/save")
    public String saveGenre(@ModelAttribute Genre genre) {
        genreService.saveGenre(genre);
        return "redirect:/genres";
    }

    @GetMapping("/edit/{id}")
    public String editGenre(@PathVariable Long id, Model model) {
        model.addAttribute("genre", genreService.getGenreById(id));
        return "genres/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return "redirect:/genres";
    }
}
