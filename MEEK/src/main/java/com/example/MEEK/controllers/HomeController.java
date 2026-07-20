package com.example.MEEK.controllers;

import com.example.MEEK.repositories.MusicRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private MusicRepository musicRepository;

    public HomeController(MusicRepository musicRepository){
        this.musicRepository = musicRepository;
    }

    @GetMapping("/")
    public String homePage(Model model){
        var allMusic = musicRepository.findAll();
        model.addAttribute("musicList",allMusic);
        return "index";
    }
}
