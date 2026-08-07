package com.example.MEEK.controllers;

import com.example.MEEK.SpotifySearchResponse;
import com.example.MEEK.services.SpotifyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MusicSearchController {
    private final SpotifyService service;

    public MusicSearchController(SpotifyService service) {
        this.service = service;
    }

    @GetMapping("/search")
    public String searchTracks(@RequestParam(required = false) String query, Model model){
        if (query != null && !query.trim().isEmpty()){
            SpotifySearchResponse response = service.searchQuery(query);
            if (response != null && response.tracks() != null){
                model.addAttribute("tracks", response.tracks().spotifyTracks());
            }
        }
        model.addAttribute("query",query);
        return "search";
    }
}
