package com.example.playlistmanager.service;

import com.example.playlistmanager.model.Album;
import com.example.playlistmanager.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    public Album getAlbumById(Long id) {
        return albumRepository.findById(id).orElse(null);
    }

    public void saveAlbum(Album album) {
        if (album.getArtist() != null && album.getArtist().getId() == null) {
            album.setArtist(null);
        }
        albumRepository.save(album);
    }

    public void deleteAlbum(Long id) {
        albumRepository.deleteById(id);
    }
}
