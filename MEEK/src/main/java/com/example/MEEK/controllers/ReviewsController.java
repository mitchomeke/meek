package com.example.MEEK.controllers;

import com.example.MEEK.repositories.MusicRepository;
import com.example.MEEK.repositories.NotificationRepository;
import com.example.MEEK.resources.Music;
import com.example.MEEK.resources.Review;
import org.h2.engine.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReviewsController {
    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/allreviews")
    public String getAllReviewsFromMusic(@RequestParam(required = true) Long musicId, Model model){
        Music music = musicRepository.findById(musicId).orElseThrow();
        model.addAttribute("music",music);
        model.addAttribute("allReviews",music.getReviews());
        return "allreviews";
    }
    @GetMapping("/reviewOf")
    public String getReviewOf(@RequestParam(required = true) Long reviewId, Model model){
        Review review = (Review) notificationRepository.findById(reviewId).orElseThrow();
        model.addAttribute("review",review);
        return "particularReview";
    }
}
