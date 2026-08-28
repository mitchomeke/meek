package com.example.MEEK.controllers;

import com.example.MEEK.repositories.*;
import com.example.MEEK.resources.Like;
import com.example.MEEK.resources.Review;
import com.example.MEEK.resources.User;
import com.example.MEEK.services.CustomUserDetailsService;
import com.example.MEEK.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.time.Instant;
import java.util.Optional;

public class AuxMethods {

    public AuxMethods() {}

    public void Like(String loggedInUserName, Long reviewId, ReviewRepository reviewRepository, UserRepository userRepository,
    NotificationRepository notificationRepository){
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        User loggedInUser = userRepository.findByUserName(loggedInUserName).orElseThrow();
        User otherUser = review.getUser();
        notificationRepository.save(new Like(loggedInUser,otherUser,review, Instant.now()));
    }
    public void unLike(String loggedInUserName, Long reviewId,ReviewRepository reviewRepository, UserRepository userRepository,
                       NotificationRepository notificationRepository, LikeRepository likeRepository){
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        User loggedInUser = userRepository.findByUserName(loggedInUserName).orElseThrow();
        Optional<Like> like = likeRepository.findByReviewAndSender(review,loggedInUser);
        notificationRepository.delete(like.get());
    }
    public void authenticate(String userName, CustomUserDetailsService userDetailsService, HttpServletRequest request){
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        SecurityContext sc = SecurityContextHolder.createEmptyContext();
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        sc.setAuthentication(auth);
        SecurityContextHolder.setContext(sc);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,sc);
    }
}
