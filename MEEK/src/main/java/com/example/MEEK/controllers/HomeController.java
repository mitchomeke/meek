package com.example.MEEK.controllers;

import com.example.MEEK.exceptions.UserNotFound;
import com.example.MEEK.repositories.MusicRepository;
import com.example.MEEK.repositories.ReviewRepository;
import com.example.MEEK.repositories.UserRepository;
import com.example.MEEK.resources.Music;
import com.example.MEEK.resources.Review;
import com.example.MEEK.resources.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class HomeController {
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final ReviewRepository reviewRepository;

    public HomeController(UserRepository userRepository, MusicRepository musicRepository, ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.musicRepository = musicRepository;
        this.reviewRepository = reviewRepository;
    }


    @GetMapping("/home")
    public String getHomePage(Principal principal, Model model){
        String name = principal.getName();
        User user = userRepository.findByUserName(name).orElseThrow(
                () -> new UserNotFound(1L)
        );
        model.addAttribute("user",user);
        model.addAttribute("friends",user.getMeekers());
        model.addAttribute("songs",musicRepository.findAll());
        model.addAttribute("userReviews",user.getReviews());
        return "home";
    }

    @PostMapping("/reviews")
    public String createReview(@RequestParam("songId") Long songId,
                               @RequestParam("rating") Integer rating,
                               @RequestParam("description") String description,
                               Principal principal){
        User user = userRepository.findByUserName(principal.getName()).orElseThrow();
        Music music = musicRepository.findById(songId).orElseThrow();

        Review review = new Review(user,music,rating,description);
        reviewRepository.save(review);
        return "redirect:/home";
    }
    @PostMapping("/reviews/edit")
    public String editReview(@RequestParam("rating") Integer rating,
                             @RequestParam("description") String description,
                             @RequestParam("reviewId") Long reviewId,
                             Principal principal){
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        User user = userRepository.findByUserName(principal.getName()).orElseThrow(
                () -> new RuntimeException("This user is not authorized to review this song.")
        );
        review.setRating(rating);
        review.setDescription(description);
        review.setUser(user);
        reviewRepository.save(review);
        return "redirect:/home";
    }
}
