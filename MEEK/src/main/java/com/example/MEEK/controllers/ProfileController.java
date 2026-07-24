package com.example.MEEK.controllers;

import com.example.MEEK.repositories.ReviewRepository;
import com.example.MEEK.repositories.UserRepository;
import com.example.MEEK.resources.Review;
import com.example.MEEK.resources.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/users")
@Controller
public class ProfileController {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public ProfileController(UserRepository userRepository, ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/{id}/profile")
    public String getProfile(@PathVariable("id") Long id, Model model, Principal principal){
        User userProfile = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found: "+ id)
        );
        User loggedInUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        model.addAttribute("profileUser",userProfile);
        model.addAttribute("userReviews",userProfile.getReviews());
        model.addAttribute("userLikes",userProfile.getLikes());
        model.addAttribute("loggedInUserLikes",loggedInUser.getLikes());
        model.addAttribute("isFollowing",loggedInUser.getMeekers().contains(userProfile));
        return "profile";
    }
    @PostMapping("/{id}/profile/unfollow")
    public String unfollowUser(@PathVariable("id") Long id, Principal principal){
        User userProfile = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found: "+ id)
        );
        User LoggedInUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        LoggedInUser.unfollowUser(userProfile);
        userRepository.save(LoggedInUser);

        return "redirect:/users/"+id+"/profile";
    }
    @PostMapping("/{id}/profile/follow")
    public String followUser(@PathVariable("id") Long id, Principal principal){
        User userProfile = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found: "+ id)
        );
        User LoggedInUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        LoggedInUser.addMeeker(userProfile);
        userRepository.save(LoggedInUser);

        return "redirect:/users/"+id+"/profile";
    }
    @PostMapping("/{id}/profile/like")
    public String like(@PathVariable("id") Long id, Principal principal, @RequestParam("reviewId") Long reviewId){
        User loggedInUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        loggedInUser.likeReview(review);
        userRepository.save(loggedInUser);

        return "redirect:/users/"+id+"/profile";
    }
    @PostMapping("/{id}/profile/unlike")
    public String unlike(@PathVariable("id") Long id, Principal principal, @RequestParam("reviewId") Long reviewId){
        User loggedInUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        loggedInUser.removeLike(review);
        userRepository.save(loggedInUser);

        return "redirect:/users/"+id+"/profile";
    }


}
