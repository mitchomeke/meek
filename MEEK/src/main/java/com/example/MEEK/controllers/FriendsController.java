package com.example.MEEK.controllers;

import com.example.MEEK.repositories.FollowRepository;
import com.example.MEEK.repositories.UserRepository;
import com.example.MEEK.resources.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class FriendsController {
    private  final UserRepository userRepository;
    private final FollowRepository followRepository;


    public FriendsController(UserRepository userRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    @GetMapping("/friends")
    public String getFriendsPage(Principal principal, Model model){
        User currentUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        model.addAttribute("friends",followRepository.getReceiversFor(currentUser).stream().toList());
        model.addAttribute("user",currentUser);
        return "friends";
    }
}
