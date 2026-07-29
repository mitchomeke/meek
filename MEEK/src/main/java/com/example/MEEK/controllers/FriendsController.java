package com.example.MEEK.controllers;

import com.example.MEEK.repositories.UserRepository;
import com.example.MEEK.resources.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class FriendsController {
    private UserRepository userRepository;

    public FriendsController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/friends")
    public String getFriendsPage(Principal principal, Model model){
        User currentUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        model.addAttribute("friends",currentUser.getMeekers());
        model.addAttribute("user",currentUser);
        return "friends";
    }
}
