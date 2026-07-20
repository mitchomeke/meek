package com.example.MEEK.controllers;

import com.example.MEEK.exceptions.UserNotFound;
import com.example.MEEK.repositories.UserRepository;
import com.example.MEEK.resources.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {
    private final UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @GetMapping("/home")
    public String homePage(Principal principal, Model model){
        String userName = principal.getName();
        User user = userRepository.findByUserName(userName).orElseThrow(
                () -> new UserNotFound(1l)
        );
        model.addAttribute("user",user);
        model.addAttribute("friends",user.getMeekers());
        return "home";
    }
}
