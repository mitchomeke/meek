package com.example.MEEK.controllers;

import com.example.MEEK.repositories.*;
import com.example.MEEK.resources.Like;
import com.example.MEEK.resources.Music;
import com.example.MEEK.resources.Review;
import com.example.MEEK.resources.User;
import org.h2.engine.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class ReviewsController {
    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/allreviews")
    public String getAllReviewsFromMusic(@RequestParam(required = true) Long musicId, Model model, Principal principal){
        Music music = musicRepository.findById(musicId).orElseThrow();
        User user = userRepository.findByUserName(principal.getName()).orElseThrow();
        if (reviewRepository.getReviewOfMusic(user,music) != null){
            model.addAttribute("reviewOfMusic",reviewRepository.getReviewOfMusic(user,music));
            model.addAttribute("music",music);
            model.addAttribute("allReviews",music.getReviews());
            model.addAttribute("songOfReviews",reviewRepository.songOfReviewsByUser(user));
        } else {
            model.addAttribute("music",music);
            model.addAttribute("allReviews",music.getReviews());
            model.addAttribute("songOfReviews",reviewRepository.songOfReviewsByUser(user));
        }
        return "allreviews";
    }
    @GetMapping("/reviewOf")
    public String getReviewOf(@RequestParam(required = true) Long likeId, Model model){
        Review review = likeRepository.findAll().stream().filter(
                l -> l.getId().equals(likeId)
        ).toList().getFirst().getReview();
        Like like = likeRepository.findById(likeId).orElseThrow();
        like.setDismissed(true);
        likeRepository.save(like);
        model.addAttribute("review",review);
        return "particularReview";
    }
}
