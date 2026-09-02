package com.example.MEEK.controllers;

import com.example.MEEK.exceptions.UserNotFound;
import com.example.MEEK.repositories.UserRepository;
import com.example.MEEK.resources.User;
import com.example.MEEK.services.CustomUserDetailsService;
import com.example.MEEK.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
public class AuthController {
    private final UserRepository userRepository;
   private final CustomUserDetailsService userDetailsService;
   private final UserService userService;
   private final AuxMethods auxMethods;

    public AuthController(UserRepository userRepository, CustomUserDetailsService userDetailsService, UserService userService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.auxMethods = new AuxMethods();
    }

    @GetMapping("/")
    public String welcomePage(){
        return "welcome";
    }

    @PostMapping("/register")
    public String createUser(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                 @RequestParam("username") String UserName,
                             @RequestParam("password") String password,
                             HttpServletRequest request){
        if (userRepository.findByUserName(UserName).isPresent()){
            return "redirect:/register?exists";
        }
        userService.registerNewUser(UserName,firstName,lastName,password);
        auxMethods.authenticate(UserName,userDetailsService,request);
        return "redirect:/home";
    }
    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }





}
