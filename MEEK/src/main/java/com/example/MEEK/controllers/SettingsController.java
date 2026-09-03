package com.example.MEEK.controllers;

import com.example.MEEK.repositories.*;
import com.example.MEEK.resources.Review;
import com.example.MEEK.resources.User;
import com.example.MEEK.services.CustomUserDetailsService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/settings")
public class SettingsController {
    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final AuxMethods auxMethods;
    private final PasswordEncoder passwordEncoder;
    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;

    public SettingsController(UserRepository userRepository, CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder, ReviewRepository reviewRepository, LikeRepository likeRepository, CommentRepository commentRepository, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.reviewRepository = reviewRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.notificationRepository = notificationRepository;
        this.auxMethods = new AuxMethods();
    }


    @GetMapping
    public String openSettings(Principal principal, Model model){
        User loggedInUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        model.addAttribute("blockedUsers",loggedInUser.getBlockedUsers());
        return "settings";
    }
    @PostMapping("/changeUserName")
    public String changeUserName(@RequestParam String currentUserName, @RequestParam String newUserName,
                                 @RequestParam String finalUserName, Principal principal,
                                 HttpServletRequest request){
        if (!currentUserName.equals(principal.getName())){
            return "redirect:/settings?userNameWrong";
        }
        if (!newUserName.equals(finalUserName)){
            return "redirect:/settings?userNameDontMatch";
        }
        if (userRepository.findByUserName(finalUserName).isPresent()){
            return "redirect:/settings?userNameAlreadyTaken";
        }
        User user = userRepository.findByUserName(principal.getName()).orElseThrow();
        user.setUserName(finalUserName);
        userRepository.save(user);

        auxMethods.authenticate(finalUserName,userDetailsService,request);
        return "redirect:/settings?userNameUpdated";
    }
    @PostMapping("/changePassword")
    public String changePassword(Principal principal,HttpServletRequest request,@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("finalPassword") String finalPassword){
        User user = userRepository.findByUserName(principal.getName()).orElseThrow();

        if (!passwordEncoder.matches(currentPassword,user.getEncryptedPassword())){
            return "redirect:/settings?passwordWrong";
        }
        if (!newPassword.equals(finalPassword)){
            return "redirect:/settings?passwordDontMatch";
        }
        if (passwordEncoder.matches(finalPassword,user.getEncryptedPassword())){
            return "redirect:/settings?sameAsBefore";
        }
        String encodedPassword = passwordEncoder.encode(finalPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        auxMethods.authenticate(user.getUserName(),userDetailsService,request);
        return "redirect:/settings?passwordUpdated";
    }
    @PostMapping("/changeBio")
    public String changeBio(Principal principal, @RequestParam("newBio") String newBio){
        if (newBio != null && newBio.length() > 150){
            return "redirect:/settings?bioTooLong";
        }
        User user = userRepository.findByUserName(principal.getName()).orElseThrow();
        user.setBio(newBio);
        userRepository.save(user);
        return "redirect:/settings?bioUpdated";
    }
    @Transactional
    @PostMapping("/clearAllReviews")
    public String clearReviews(Principal principal){
        User user = userRepository.findByUserName(principal.getName()).orElseThrow();
        List<Review> reviewsByUser = reviewRepository.getUserReviews(user);
        reviewsByUser.forEach(
                r -> {
                 commentRepository.deleteCommentsOfReview(r);
                 likeRepository.deleteLikesOfReview(r);
                }
        );
        reviewRepository.deleteReviewsByUser(user);
        userRepository.save(user);
        return "redirect:/settings?reviewsCleared";
    }
    @PostMapping("/clearAllLikes")
    public String clearLikes(Principal principal){
        User user = userRepository.findByUserName(principal.getName()).orElseThrow();
        likeRepository.deleteLikesBySender(user);
        userRepository.save(user);
        return "redirect:/settings?likesCleared";
    }

    @Transactional
    @PostMapping("/deleteAccount")
    public String deleteUser(Principal principal){
        User user = userRepository.findByUserName(principal.getName()).orElseThrow();
        notificationRepository.deleteAllByUser(user);
        reviewRepository.deleteReviewsByUser(user);
        userRepository.delete(user);
        return "redirect:/register";
    }

}
