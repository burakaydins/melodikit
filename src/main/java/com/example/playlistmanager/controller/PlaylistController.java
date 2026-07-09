package com.example.playlistmanager.controller;

import com.example.playlistmanager.model.Playlist;
import com.example.playlistmanager.service.PlaylistService;
import com.example.playlistmanager.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private SongService songService;

    @GetMapping
    public String listPlaylists(Model model) {
        model.addAttribute("playlists", playlistService.getAllPlaylists());
        return "playlists/list";
    }

    @GetMapping("/{id}")
    public String playlistDetail(@PathVariable Long id, Model model) {
        Playlist playlist = playlistService.getPlaylistById(id);
        model.addAttribute("playlist", playlist);
        model.addAttribute("totalDuration", playlistService.getTotalDuration(playlist));
        return "playlists/detail";
    }

    @GetMapping("/new")
    public String newPlaylistForm(Model model) {
        model.addAttribute("playlist", new Playlist());
        model.addAttribute("songs", songService.getAllSongs());
        return "playlists/form";
    }

    @PostMapping("/save")
    public String savePlaylist(@ModelAttribute Playlist playlist,
                               @RequestParam(required = false) List<Long> songIds,
                               Model model) {
        try {
            playlistService.savePlaylist(playlist, songIds);
        } catch (IllegalArgumentException exception) {
            model.addAttribute("error", exception.getMessage());
            model.addAttribute("playlist", playlist);
            model.addAttribute("songs", songService.getAllSongs());
            return "playlists/form";
        }
        return "redirect:/playlists";
    }

    @GetMapping("/edit/{id}")
    public String editPlaylist(@PathVariable Long id, Model model) {
        model.addAttribute("playlist", playlistService.getPlaylistById(id));
        model.addAttribute("songs", songService.getAllSongs());
        return "playlists/form";
    }

    @GetMapping("/delete/{id}")
    public String deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);
        return "redirect:/playlists";
    }

    @PostMapping("/{playlistId}/remove-song/{songId}")
    public String removeSong(@PathVariable Long playlistId, @PathVariable Long songId) {
        playlistService.removeSongFromPlaylist(playlistId, songId);
        return "redirect:/playlists/" + playlistId;
    }
}
