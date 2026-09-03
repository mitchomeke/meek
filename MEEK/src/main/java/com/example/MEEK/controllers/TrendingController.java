package com.example.MEEK.controllers;

import com.example.MEEK.exceptions.UserNotFound;
import com.example.MEEK.repositories.*;
import com.example.MEEK.resources.Like;
import com.example.MEEK.resources.Music;
import com.example.MEEK.resources.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Comparator;

@Controller
@RequestMapping("/trending")
public class TrendingController {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final ReviewRepository reviewRepository;
    private final FollowRepository followRepository;
    private final LikeRepository likeRepository;
    private final MusicRepository musicRepository;
    private AuxMethods auxMethods;

    public TrendingController(UserRepository userRepository, NotificationRepository notificationRepository, ReviewRepository reviewRepository, FollowRepository followRepository, LikeRepository likeRepository, MusicRepository musicRepository) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.reviewRepository = reviewRepository;
        this.followRepository = followRepository;
        this.likeRepository = likeRepository;
        this.musicRepository = musicRepository;
        auxMethods = new AuxMethods();
    }

    @GetMapping
    public String getTrendingPage(Principal principal, Model model){
        String name = principal.getName();
        User user = userRepository.findByUserName(name).orElseThrow(
                () -> new UserNotFound(1L)
        );
        model.addAttribute("activeTab","trending");
        auxMethods.pageInit(model, user, notificationRepository, reviewRepository, followRepository, likeRepository);
        model.addAttribute("musics",musicRepository.findAll()
                .stream().filter(music -> music.getRating() > 0).
                sorted(Comparator.comparingDouble(Music::getRating).reversed()).toList());
        model.addAttribute("musicOfReviews",reviewRepository.songOfReviewsByUser(user).stream().toList());
        model.addAttribute("userReviews",reviewRepository.getUserReviews(user).stream().toList());

        return "trending";
    }


}
