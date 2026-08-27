package com.example.MEEK.controllers;

import com.example.MEEK.repositories.*;
import com.example.MEEK.resources.Like;
import com.example.MEEK.resources.Review;
import com.example.MEEK.resources.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

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
}
