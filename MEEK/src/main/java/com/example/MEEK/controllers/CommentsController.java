package com.example.MEEK.controllers;

import com.example.MEEK.repositories.CommentRepository;
import com.example.MEEK.repositories.NotificationRepository;
import com.example.MEEK.repositories.ReviewRepository;
import com.example.MEEK.repositories.UserRepository;
import com.example.MEEK.resources.Comment;
import com.example.MEEK.resources.Review;
import com.example.MEEK.resources.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Controller
public class CommentsController {
    private final NotificationRepository notificationRepository;
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public CommentsController(NotificationRepository notificationRepository, CommentRepository commentRepository, ReviewRepository reviewRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.commentRepository = commentRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }
    @GetMapping("/comments")
    public String allComments(@RequestParam("reviewId") Long reviewId, Model model, Principal principal){
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        User loggedInUser = userRepository.findByUserName(principal.getName()).orElseThrow();
        List<Comment> commentsOfReview = commentRepository.findAllCommentsOfReview(review);
        model.addAttribute("review",review);
        model.addAttribute("loggedInUser",loggedInUser);
        model.addAttribute("comments",commentsOfReview);
        return "comments";
    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam("reviewId") Long reviewId, @RequestParam("description") String description
            , @RequestParam("senderId") Long senderId){
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        User sender = userRepository.findById(senderId).orElseThrow();

        LocalDate commentDate = LocalDate.now();
        Instant commentInstant = commentDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Comment comment1 = new Comment(review,description,sender,commentInstant);
        notificationRepository.save(comment1);
        return "redirect:/comments?reviewId="+reviewId;
    }
}
