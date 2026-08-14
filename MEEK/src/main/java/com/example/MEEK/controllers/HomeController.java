package com.example.MEEK.controllers;

import com.example.MEEK.exceptions.UserNotFound;
import com.example.MEEK.repositories.MusicRepository;
import com.example.MEEK.repositories.NotificationRepository;
import com.example.MEEK.repositories.ReviewRepository;
import com.example.MEEK.repositories.UserRepository;
import com.example.MEEK.resources.Music;
import com.example.MEEK.resources.Notification;
import com.example.MEEK.resources.Review;
import com.example.MEEK.resources.User;
import com.example.MEEK.services.SpotifyService;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;

@Controller
public class HomeController {
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final ReviewRepository reviewRepository;
    private final NotificationRepository notificationRepository;

    public HomeController(UserRepository userRepository, MusicRepository musicRepository, ReviewRepository reviewRepository, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.musicRepository = musicRepository;
        this.reviewRepository = reviewRepository;
        this.notificationRepository = notificationRepository;
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
        model.addAttribute("likes",user.getLikes());
        model.addAttribute("musics",musicRepository.findAll()
                .stream().filter(music -> music.getRating() > 0).
                sorted(Comparator.comparingDouble(Music::getRating).reversed()).toList());

        model.addAttribute("friendReviews",
                reviewRepository.findByUserIn(user.getMeekers()));
        model.addAttribute("musicOfReviews",user.getReviews().stream().map(
                Review::getMusic
        ).toList());
        model.addAttribute("notifications",user.getNotifications());
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

    @PostMapping("/home/explore/like")
    public String like(Principal principal, @RequestParam("reviewId") Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow();


        User loggedInUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        User otherUser = review.getUser();

        review.setReceiver(otherUser);
        review.setSender(loggedInUser);

        loggedInUser.likeReview(review);
        otherUser.addNotifications(review);

        userRepository.save(loggedInUser);
        userRepository.save(otherUser);
        notificationRepository.save(review);

        return "redirect:/home";
    }
    @PostMapping("/home/explore/unlike")
    public String unlike( Principal principal, @RequestParam("reviewId") Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        User loggedInUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        User otherUser = review.getUser();

        loggedInUser.removeLike(review);
        otherUser.removeNotification(review);

        userRepository.save(loggedInUser);
        userRepository.save(otherUser);

        return "redirect:/home";
    }
}
