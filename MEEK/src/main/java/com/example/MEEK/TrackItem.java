package com.example.MEEK;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;

public record TrackItem(
        @JsonProperty("trackId")
        Long trackId,
        @JsonProperty("artistName")
        String artistName,
        @JsonProperty("collectionName")
        String collectionName,
        @JsonProperty("trackName")
        String trackName,
        @JsonProperty("kind")
        String kind,
        @JsonProperty("wrapperType")
        String wrapperType,
        @JsonProperty("artworkUrl30")
        String artworkUrl30,
        @JsonProperty("releaseDate")
        String releaseDate,
        @JsonProperty("artistId")
        Long artistId,
        @JsonProperty("collectionId")
        Long collectionId
){}
