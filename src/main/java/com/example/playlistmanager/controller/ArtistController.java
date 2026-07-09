package com.example.playlistmanager.controller;

import com.example.playlistmanager.model.Artist;
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
@RequestMapping("/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping
    public String listArtists(Model model) {
        model.addAttribute("artists", artistService.getAllArtists());
        return "artists/list";
    }

    @GetMapping("/new")
    public String newArtistForm(Model model) {
        model.addAttribute("artist", new Artist());
        return "artists/form";
    }

    @GetMapping("/{id}")
    public String artistDetail(@PathVariable Long id, Model model) {
        model.addAttribute("artist", artistService.getArtistById(id));
        return "artists/detail";
    }

    @PostMapping("/save")
    public String saveArtist(@ModelAttribute Artist artist) {
        artistService.saveArtist(artist);
        return "redirect:/artists";
    }

    @GetMapping("/edit/{id}")
    public String editArtist(@PathVariable Long id, Model model) {
        model.addAttribute("artist", artistService.getArtistById(id));
        return "artists/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteArtist(@PathVariable Long id) {
        artistService.deleteArtist(id);
        return "redirect:/artists";
    }
}
