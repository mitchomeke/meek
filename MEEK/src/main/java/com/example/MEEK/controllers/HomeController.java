package com.example.MEEK.controllers;

import com.example.MEEK.exceptions.UserNotFound;
import com.example.MEEK.repositories.*;
import com.example.MEEK.resources.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.util.Comparator;

@Controller
public class HomeController {
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final ReviewRepository reviewRepository;
    private final NotificationRepository notificationRepository;
    private final LikeRepository likeRepository;
    private final FollowRepository followRepository;

    public HomeController(UserRepository userRepository, MusicRepository musicRepository, ReviewRepository reviewRepository, NotificationRepository notificationRepository, LikeRepository likeRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.musicRepository = musicRepository;
        this.reviewRepository = reviewRepository;
        this.notificationRepository = notificationRepository;
        this.likeRepository = likeRepository;
        this.followRepository = followRepository;
    }


    @GetMapping("/home")
    public String getHomePage(Principal principal, Model model){
        LoadHomePage(principal, model);
        return "home";
    }

    @PostMapping("/reviews")
    public String createReview(@RequestParam("songId") Long songId,
                               @RequestParam("rating") Integer rating,
                               @RequestParam("description") String description,
                               Principal principal,Model model){
        User user = userRepository.findByUserName(principal.getName()).orElseThrow();
        Music music = musicRepository.findById(songId).orElseThrow();

        Review review = new Review(user,music,rating,description);
        reviewRepository.save(review);
        userRepository.save(user);

      return "redirect:/allreviews?musicId="+music.getId();
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
        userRepository.save(user);

        return "redirect:/allreviews?musicId="+review.getMusic().getId();
    }

    @PostMapping("/home/explore/like")
    public String like(Principal principal, @RequestParam("reviewId") Long reviewId, Model model){
        Review review = reviewRepository.findById(reviewId).orElseThrow();

        User loggedInUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        User otherUser = review.getUser();

        notificationRepository.save(new Like(loggedInUser,otherUser,review, Instant.now()));
        userRepository.save(loggedInUser);
        userRepository.save(otherUser);

        return "redirect:/home";
    }
    @PostMapping("/home/explore/unlike")
    public String unlike( Principal principal, @RequestParam("reviewId") Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        User loggedInUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        User otherUser = review.getUser();

        Like like = likeRepository.findAll().stream()
                .filter(l -> l.getReview().equals(review)
                        && l.getSender().equals(loggedInUser)
                && l.getReceiver().equals(otherUser))
                .findFirst()
                .orElseThrow();

        notificationRepository.delete(like);
        userRepository.save(loggedInUser);
        userRepository.save(otherUser);
        return "redirect:/home";
    }
    private void LoadHomePage(Principal principal, Model model){
        String name = principal.getName();
        User user = userRepository.findByUserName(name).orElseThrow(
                () -> new UserNotFound(1L)
        );
        model.addAttribute("user",user);
        model.addAttribute("friends",followRepository.getReceiversFor(user).stream().toList());
        model.addAttribute("songs",musicRepository.findAll());
        model.addAttribute("userReviews",reviewRepository.getUserReviews(user).stream().toList());
        model.addAttribute("reviewOfLikes",likeRepository.findBySender(user).stream().map(Like::getReview).toList());
        model.addAttribute("likes",likeRepository.findBySender(user));
        model.addAttribute("musics",musicRepository.findAll()
                .stream().filter(music -> music.getRating() > 0).
                sorted(Comparator.comparingDouble(Music::getRating).reversed()).toList());

        model.addAttribute("friendReviews",
                reviewRepository.findByUserIn(followRepository.getReceiversFor(user).stream().toList()));
        model.addAttribute("musicOfReviews",reviewRepository.songOfReviewsByUser(user).stream().toList());
        model.addAttribute("notifications",notificationRepository.findByReceiver(user).stream().filter(
                n -> !n.isDismissed()
        ).toList());
    }



}
