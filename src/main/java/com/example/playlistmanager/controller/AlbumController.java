package com.example.playlistmanager.controller;

import com.example.playlistmanager.model.Album;
import com.example.playlistmanager.service.AlbumService;
import com.example.playlistmanager.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private ArtistService artistService;

    @GetMapping
    public String listAlbums(Model model) {
        model.addAttribute("albums", albumService.getAllAlbums());
        return "albums/list";
    }

    @GetMapping("/new")
    public String newAlbumForm(Model model) {
        model.addAttribute("album", new Album());
        model.addAttribute("artists", artistService.getAllArtists());
        return "albums/form";
    }

    @GetMapping("/{id}")
    public String albumDetail(@PathVariable Long id, Model model) {
        model.addAttribute("album", albumService.getAlbumById(id));
        return "albums/detail";
    }

    @PostMapping("/save")
    public String saveAlbum(@ModelAttribute Album album) {
        albumService.saveAlbum(album);
        return "redirect:/albums";
    }

    @GetMapping("/edit/{id}")
    public String editAlbum(@PathVariable Long id, Model model) {
        model.addAttribute("album", albumService.getAlbumById(id));
        model.addAttribute("artists", artistService.getAllArtists());
        return "albums/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
        return "redirect:/albums";
    }
}
