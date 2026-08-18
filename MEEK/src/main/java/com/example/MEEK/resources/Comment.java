package com.example.MEEK.resources;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.time.Instant;

@Entity
public class Comment extends Notification {
    @ManyToOne
    private Review review;
    private String description;
    private Instant createdAt;

    public Comment(Review review, String description, User sender, Instant createdAt) {
        this.review = review;
        this.description = description;
        this.sender = sender;
        this.createdAt = createdAt;
    }
    public Comment(){}

    @Override
    public User getReceiver(){
        return review.getUser();
    }

    public Review getReview() {
        return review;
    }
    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
