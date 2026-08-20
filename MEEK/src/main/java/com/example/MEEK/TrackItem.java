package com.example.MEEK;

import com.fasterxml.jackson.annotation.JsonProperty;

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
        String artworkUrl30
){}
