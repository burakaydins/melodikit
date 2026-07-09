package com.example.playlistmanager.service;

import com.example.playlistmanager.model.Song;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class PreviewAudioService {

    private static final int MINIMUM_MATCH_SCORE = 120;

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public PreviewAudioService() {
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public Optional<String> findPreviewUrl(Song song) {
        if (song == null || song.getTitle() == null || song.getArtist() == null || song.getArtist().getName() == null) {
            return Optional.empty();
        }

        List<String> queries = List.of(
                "track:\"" + song.getTitle() + "\" artist:\"" + song.getArtist().getName() + "\"",
                song.getTitle() + " " + song.getArtist().getName()
        );

        for (String query : queries) {
            Optional<String> previewUrl = findPreviewUrl(song, query);
            if (previewUrl.isPresent()) {
                return previewUrl;
            }
        }

        return Optional.empty();
    }

    private Optional<String> findPreviewUrl(Song song, String query) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        URI uri = URI.create("https://api.deezer.com/search?q=" + encodedQuery + "&limit=10");

        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(Duration.ofSeconds(8))
                .header("User-Agent", "MelodiKit/1.0")
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return Optional.empty();
            }

            DeezerSearchResponse searchResponse = objectMapper.readValue(response.body(), DeezerSearchResponse.class);
            return searchResponse.tracks().stream()
                    .filter(track -> hasText(track.preview()))
                    .map(track -> new ScoredTrack(track, score(track, song)))
                    .filter(scoredTrack -> scoredTrack.score() >= MINIMUM_MATCH_SCORE)
                    .max(Comparator.comparingInt(ScoredTrack::score))
                    .map(scoredTrack -> scoredTrack.track().preview());
        } catch (IOException | InterruptedException exception) {
            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return Optional.empty();
        }
    }

    private int score(DeezerTrack track, Song song) {
        String trackTitle = normalize(track.title());
        String songTitle = normalize(song.getTitle());
        String trackArtist = normalize(track.artist() != null ? track.artist().name() : "");
        String songArtist = normalize(song.getArtist() != null ? song.getArtist().getName() : "");
        String trackAlbum = normalize(track.album() != null ? track.album().title() : "");
        String songAlbum = normalize(song.getAlbum() != null ? song.getAlbum().getTitle() : "");

        int score = 0;
        if (trackTitle.equals(songTitle)) {
            score += 120;
        } else if (trackTitle.startsWith(songTitle) || trackTitle.contains(songTitle + " ")) {
            score += 85;
        } else if (trackTitle.contains(songTitle) || songTitle.contains(trackTitle)) {
            score += 55;
        }

        if (trackArtist.equals(songArtist) || trackArtist.contains(songArtist)) {
            score += 80;
        } else if (!songArtist.isBlank() && songArtist.split(" ").length > 1) {
            boolean allArtistPartsMatch = List.of(songArtist.split(" ")).stream()
                    .filter(part -> part.length() > 1)
                    .allMatch(trackArtist::contains);
            if (allArtistPartsMatch) {
                score += 55;
            }
        }

        if (!songAlbum.isBlank() && (trackAlbum.equals(songAlbum) || trackAlbum.contains(songAlbum))) {
            score += 20;
        }

        if (trackTitle.contains(" live ") || trackTitle.endsWith(" live") || trackTitle.contains(" remix ")) {
            score -= 15;
        }

        return score;
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        String lower = value.toLowerCase(Locale.ROOT)
                .replace("ı", "i")
                .replace("’", "")
                .replace("'", "");
        String normalized = Normalizer.normalize(lower, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalized.replaceAll("[^a-z0-9]+", " ").trim();
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private record ScoredTrack(DeezerTrack track, int score) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record DeezerSearchResponse(List<DeezerTrack> data) {
        private List<DeezerTrack> tracks() {
            return data == null ? List.of() : data;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record DeezerTrack(String title, String preview, DeezerArtist artist, DeezerAlbum album) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record DeezerArtist(String name) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record DeezerAlbum(String title) {
    }
}
