package com.example.MEEK;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SpotifySearchResponse(TracksContainer tracks) {
    public record TracksContainer(List<SpotifyTracks> spotifyTracks){}
    public record SpotifyTracks(
            String id,
            String name,
            List<SpotifyArtist> spotifyArtists,
            SpotifyAlbum spotifyAlbum
    ){}
    public record SpotifyArtist(
            String name
    ){}
    public record SpotifyAlbum(
            String name,
            @JsonProperty("images") List<SpotifyImage> spotifyImages
    ){}
    public record SpotifyImage(
            String url
    ){}
}
