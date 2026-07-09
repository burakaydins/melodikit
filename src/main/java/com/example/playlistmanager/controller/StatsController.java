package com.example.playlistmanager.controller;

import com.example.playlistmanager.model.Genre;
import com.example.playlistmanager.model.Song;
import com.example.playlistmanager.repository.ListenHistoryRepository;
import com.example.playlistmanager.repository.PlaylistRepository;
import com.example.playlistmanager.repository.SongRepository;
import com.example.playlistmanager.service.SongService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class StatsController {

    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;
    private final ListenHistoryRepository listenHistoryRepository;
    private final SongService songService;

    public StatsController(SongRepository songRepository,
                           PlaylistRepository playlistRepository,
                           ListenHistoryRepository listenHistoryRepository,
                           SongService songService) {
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
        this.listenHistoryRepository = listenHistoryRepository;
        this.songService = songService;
    }

    @GetMapping("/stats")
    public String stats(Model model) {
        var songs = songRepository.findAll();
        int totalPlays = songs.stream().mapToInt(Song::getPlayCount).sum();
        int totalDuration = songs.stream().mapToInt(Song::getDuration).sum();
        Song mostPlayedSong = songs.stream().max(Comparator.comparingInt(Song::getPlayCount)).orElse(null);

        String topArtist = songs.stream()
                .filter(song -> song.getArtist() != null)
                .collect(Collectors.groupingBy(song -> song.getArtist().getName(), Collectors.summingInt(Song::getPlayCount)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Veri yok");

        String topGenre = songs.stream()
                .map(Song::getGenre)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Genre::getName, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Veri yok");

        model.addAttribute("songCount", songs.size());
        model.addAttribute("playlistCount", playlistRepository.count());
        model.addAttribute("totalPlays", totalPlays);
        model.addAttribute("totalDuration", totalDuration);
        model.addAttribute("mostPlayedSong", mostPlayedSong);
        model.addAttribute("topArtist", topArtist);
        model.addAttribute("topGenre", topGenre);
        model.addAttribute("mostPlayedSongs", songService.getMostPlayedSongs(5));
        model.addAttribute("listenHistory", listenHistoryRepository.findLatest(10));
        return "stats/index";
    }
}
