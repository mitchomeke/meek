package com.example.MEEK.controllers;

import com.example.MEEK.ItunesSearchResponse;
import com.example.MEEK.services.ItunesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MusicSearchController {
    private final ItunesService service;

    public MusicSearchController(ItunesService service) {
        this.service = service;
    }

    @GetMapping("/search")
    public String searchTracks(@RequestParam(required = false) String query, Model model){
        if (query != null && !query.trim().isEmpty()){
            ItunesSearchResponse response = service.searchQuery(query);
            if (response != null && response.trackItemList() != null){
                model.addAttribute("tracks", response.trackItemList().stream().toList());
            }
        }
        model.addAttribute("query",query);
        return "search";
    }
}
