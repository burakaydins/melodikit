package com.example.playlistmanager.service;

import com.example.playlistmanager.model.Genre;
import com.example.playlistmanager.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre getGenreById(Long id) {
        return genreRepository.findById(id).orElse(null);
    }

    public void saveGenre(Genre genre) {
        if (genre.getName() != null) {
            genre.setName(genre.getName().trim());
        }
        if (genre.getImageUrl() != null) {
            genre.setImageUrl(genre.getImageUrl().trim());
        }
        genreRepository.save(genre);
    }

    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }
}
