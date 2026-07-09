package com.example.playlistmanager.controller;

import com.example.playlistmanager.model.AppUser;
import com.example.playlistmanager.repository.ListenHistoryRepository;
import com.example.playlistmanager.repository.PlaylistRepository;
import com.example.playlistmanager.repository.SongRepository;
import com.example.playlistmanager.service.SongService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;
    private final ListenHistoryRepository listenHistoryRepository;
    private final SongService songService;

    public ProfileController(SongRepository songRepository,
                             PlaylistRepository playlistRepository,
                             ListenHistoryRepository listenHistoryRepository,
                             SongService songService) {
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
        this.listenHistoryRepository = listenHistoryRepository;
        this.songService = songService;
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Object user = session.getAttribute("currentUser");
        if (!(user instanceof AppUser appUser)) {
            return "redirect:/login";
        }

        var songs = songRepository.findAll();
        int totalPlays = songs.stream().mapToInt(song -> song.getPlayCount()).sum();
        model.addAttribute("profileUser", appUser);
        model.addAttribute("favoriteCount", songRepository.findByFavoriteTrue().size());
        model.addAttribute("playlistCount", playlistRepository.count());
        model.addAttribute("totalPlays", totalPlays);
        model.addAttribute("mostPlayedSongs", songService.getMostPlayedSongs(5));
        model.addAttribute("listenHistory", listenHistoryRepository.findLatest(8));
        return "profile/index";
    }
}
