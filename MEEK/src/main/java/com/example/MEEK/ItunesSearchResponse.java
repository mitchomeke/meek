package com.example.MEEK;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ItunesSearchResponse(
        @JsonProperty("resultCount")
        int countNumber,
        @JsonProperty("results")
        List<TrackItem> trackItemList) {}
