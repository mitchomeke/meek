package com.example.MEEK.controllers;

import com.example.MEEK.ItunesSearchResponse;
import com.example.MEEK.TrackItem;
import com.example.MEEK.repositories.MusicRepository;
import com.example.MEEK.resources.Music;
import com.example.MEEK.resources.Song;
import com.example.MEEK.services.ItunesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class MusicSearchController {
    private final ItunesService service;
    @Autowired
    private MusicRepository musicRepository;

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

    @PostMapping("/analyzeMusic")
    public String analyzeMusic(@RequestParam("trackId") Long trackId){
        TrackItem item = service.searchTrack(trackId);
        if (item == null){
            System.err.println("Track Item not found.");
        }
        if (musicRepository.getMusicByMusicNameAndArtistName(item.trackName(),item.artistName()) == null){
            Song song = new Song(item.trackName(), LocalDate.parse(item.releaseDate().substring(0,10)),item.artistName(),3);
            musicRepository.save(song);
            return "redirect:/allreviews?musicId="+song.getId();
        } else {
            Music music = musicRepository.getMusicByMusicNameAndArtistName(item.trackName(),item.artistName());
            return "redirect:/allreviews?musicId="+music.getId();
        }
    }

}
