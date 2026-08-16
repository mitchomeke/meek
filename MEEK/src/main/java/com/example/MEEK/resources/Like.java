package com.example.MEEK.resources;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.Instant;

@Entity
public class Like extends Notification{
    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    public Like(User sender, User receiver, Review review, Instant exactTime){
        this.sender = sender;
        this.receiver = receiver;
        this.review = review;
        this.exactTime = exactTime;
    }

    public Like(){
        super();
    }
    public Review getReview() {
        return review;
    }
    public void setReview(Review review) {
        this.review = review;
    }
    public Long getId(){
        return id;
    }
}
