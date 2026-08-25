package com.example.MEEK.controllers;

import com.example.MEEK.repositories.*;
import com.example.MEEK.resources.Follow;
import com.example.MEEK.resources.Like;
import com.example.MEEK.resources.Review;
import com.example.MEEK.resources.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.util.Optional;

@RequestMapping("/users")
@Controller
public class ProfileController {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final NotificationRepository notificationRepository;
    private final LikeRepository likeRepository;
    private final FollowRepository followRepository;

    public ProfileController(UserRepository userRepository, ReviewRepository reviewRepository, NotificationRepository notificationRepository, LikeRepository likeRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.notificationRepository = notificationRepository;
        this.likeRepository = likeRepository;
        this.followRepository = followRepository;
    }

    @GetMapping("/{id}/profile")
    public String getProfile(@PathVariable("id") Long id, Model model, Principal principal){
        User userProfile = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found: "+ id)
        );
        User loggedInUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        Optional<Follow> follow = followRepository.findAll().stream().filter(
                f -> (
                        f.getSender().equals(userProfile)
                        &&
                        f.getReceiver().equals(loggedInUser))).findAny();
        if (follow.isPresent()){
            follow.get().setDismissed(true);
            followRepository.save(follow.get());
        }

        model.addAttribute("loggedInUser",loggedInUser);
        model.addAttribute("profileUser",userProfile);
        model.addAttribute("userReviews",reviewRepository.findAll().stream().filter(
                r -> r.getUser().equals(userProfile)
                ).toList());
        model.addAttribute("userLikes",likeRepository.findBySender(userProfile));
        model.addAttribute("loggedInUserReviewOfLikes",likeRepository.findBySender(loggedInUser).stream()
                .map(Like::getReview).toList());
        model.addAttribute("isFollowing",followRepository.getReceiversFor(loggedInUser).stream().toList().contains(userProfile));
        return "profile";
    }
    @PostMapping("/{id}/profile/unfollow")
    public String unfollowUser(@PathVariable("id") Long id, Principal principal){
        User userProfile = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found: "+ id)
        );
        User LoggedInUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        Follow follow = followRepository.findAll().stream().filter(
                f ->
                 (f.getSender().equals(LoggedInUser) && f.getReceiver().equals(userProfile))
        ).findAny().orElseThrow();
        followRepository.delete(follow);

        userRepository.save(LoggedInUser);

        return "redirect:/users/"+id+"/profile";
    }
    @PostMapping("/{id}/profile/follow")
    public String followUser(@PathVariable("id") Long id, Principal principal){
        User userProfile = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found: "+ id)
        );
        User LoggedInUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        Follow follow = new Follow(LoggedInUser,userProfile);
        followRepository.save(follow);

        userRepository.save(LoggedInUser);

        return "redirect:/users/"+id+"/profile";
    }
    @PostMapping("/{id}/profile/like")
    public String like(@PathVariable("id") Long id, Principal principal, @RequestParam("reviewId") Long reviewId){
        User loggedInUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        User otherUser = userRepository.findById(id).orElseThrow();
        Review review = reviewRepository.findById(reviewId).orElseThrow();

        notificationRepository.save(new Like(loggedInUser,otherUser,review, Instant.now()));

        return "redirect:/users/"+id+"/profile";
    }
    @PostMapping("/{id}/profile/unlike")
    public String unlike(@PathVariable("id") Long id, Principal principal, @RequestParam("reviewId") Long reviewId){
        User loggedInUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        User otherUser = userRepository.findById(id).orElseThrow();
        Review review = reviewRepository.findById(reviewId).orElseThrow();

        Like like = likeRepository.findAll().stream().filter(
                l ->
                    l.getReview().equals(review)
                    && l.getReceiver().equals(otherUser)
                    && l.getSender().equals(loggedInUser)

        ).toList().getFirst();

        notificationRepository.delete(like);
        return "redirect:/users/"+id+"/profile";
    }


}
