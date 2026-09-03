package com.example.MEEK.controllers;

import com.example.MEEK.exceptions.UserNotFound;
import com.example.MEEK.repositories.*;
import com.example.MEEK.resources.Like;
import com.example.MEEK.resources.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/explore")
public class ExploreController {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final ReviewRepository reviewRepository;
    private final FollowRepository followRepository;
    private final LikeRepository likeRepository;
    private AuxMethods auxMethods;

    public ExploreController(UserRepository userRepository, NotificationRepository notificationRepository, ReviewRepository reviewRepository, FollowRepository followRepository, LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.reviewRepository = reviewRepository;
        this.followRepository = followRepository;
        this.likeRepository = likeRepository;
        auxMethods = new AuxMethods();
    }

    @GetMapping
    public String getExplorePage(Principal principal, Model model){
        String name = principal.getName();
        User user = userRepository.findByUserName(name).orElseThrow(
                () -> new UserNotFound(1L)
        );
        model.addAttribute("activeTab","explore");
        auxMethods.pageInit(model, user, notificationRepository, reviewRepository, followRepository, likeRepository);
        return "explore";
    }


}
