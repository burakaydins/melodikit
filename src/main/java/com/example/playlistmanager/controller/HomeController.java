package com.example.playlistmanager.controller;

import com.example.playlistmanager.repository.AlbumRepository;
import com.example.playlistmanager.repository.ArtistRepository;
import com.example.playlistmanager.repository.GenreRepository;
import com.example.playlistmanager.repository.PlaylistRepository;
import com.example.playlistmanager.repository.SongRepository;
import com.example.playlistmanager.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongService songService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("songCount", songRepository.count());
        model.addAttribute("artistCount", artistRepository.count());
        model.addAttribute("albumCount", albumRepository.count());
        model.addAttribute("genreCount", genreRepository.count());
        model.addAttribute("playlistCount", playlistRepository.count());
        model.addAttribute("favoriteCount", songRepository.findByFavoriteTrue().size());
        model.addAttribute("latestSongs", songService.getLatestSongs(4));
        model.addAttribute("topRatedSongs", songService.getTopRatedSongs(4));
        model.addAttribute("mostPlayedSongs", songService.getMostPlayedSongs(4));
        model.addAttribute("featuredPlaylists", playlistRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream().limit(3).toList());
        return "index";
    }
}
