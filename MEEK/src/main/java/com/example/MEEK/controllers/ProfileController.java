package com.example.MEEK.controllers;

import com.example.MEEK.repositories.UserRepository;
import com.example.MEEK.resources.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/users")
@Controller
public class ProfileController {
    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}/profile")
    public String getProfile(@PathVariable("id") Long id, Model model){
        User userProfile = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found: "+ id)
        );
        model.addAttribute("profileUser",userProfile);
        return "profile";
    }
}
