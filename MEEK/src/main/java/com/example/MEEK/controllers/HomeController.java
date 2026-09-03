package com.example.MEEK.controllers;

import com.example.MEEK.exceptions.UserNotFound;
import com.example.MEEK.repositories.*;
import com.example.MEEK.resources.*;
import com.example.MEEK.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final AuxMethods auxMethods;
    private final FileStorageService fileStorageService;

    public HomeController(UserRepository userRepository, MusicRepository musicRepository, ReviewRepository reviewRepository, NotificationRepository notificationRepository, LikeRepository likeRepository, FollowRepository followRepository, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.musicRepository = musicRepository;
        this.reviewRepository = reviewRepository;
        this.notificationRepository = notificationRepository;
        this.likeRepository = likeRepository;
        this.followRepository = followRepository;
        this.fileStorageService = fileStorageService;
        this.auxMethods = new AuxMethods();
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
    @PostMapping("/updatePhoto")
    public String updatePhoto(Principal principal, @RequestParam(value = "customPhoto", required = false) MultipartFile customPhoto) throws Exception {
        User user = userRepository.findByUserName(principal.getName()).orElseThrow();
        if (customPhoto != null && !customPhoto.isEmpty()){
            String savedFileName = fileStorageService.store(customPhoto);
            user.setDisplayPhoto("/uploads/avatars/"+savedFileName);
            userRepository.save(user);
        }
        return "redirect:/home";
    }

    @PostMapping("/home/explore/like")
    public String like(Principal principal, @RequestParam("reviewId") Long reviewId){
        auxMethods.Like(principal.getName(),reviewId,reviewRepository,userRepository,notificationRepository);
        return "redirect:/home";
    }
    @PostMapping("/home/explore/unlike")
    public String unlike( Principal principal, @RequestParam("reviewId") Long reviewId){
        auxMethods.unLike(principal.getName(),reviewId,reviewRepository,userRepository,notificationRepository,likeRepository);
        return "redirect:/home";
    }
    private void LoadHomePage(Principal principal, Model model){
        String name = principal.getName();
        User user = userRepository.findByUserName(name).orElseThrow(
                () -> new UserNotFound(1L)
        );
        model.addAttribute("activeTab","home");
        model.addAttribute("user",user);
        model.addAttribute("friends",followRepository.getReceiversFor(user).stream().toList());
        model.addAttribute("userReviews",reviewRepository.getUserReviews(user).stream().toList());
        model.addAttribute("likes",likeRepository.findBySender(user));
        model.addAttribute("notifications",notificationRepository.findByReceiver(user).stream().filter(
                n -> !n.isDismissed()
        ).toList());
    }



}
