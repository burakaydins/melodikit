package com.example.playlistmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class ListenHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Song song;

    private LocalDateTime listenedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Song getSong() { return song; }
    public void setSong(Song song) { this.song = song; }

    public LocalDateTime getListenedAt() { return listenedAt; }
    public void setListenedAt(LocalDateTime listenedAt) { this.listenedAt = listenedAt; }
}
