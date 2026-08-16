package com.example.MEEK.resources;

import jakarta.persistence.Entity;

@Entity
public class Follow extends Notification{

    public Follow(User sender, User receiver){
        this.sender = sender;
        this.receiver = receiver;
    }
    public Follow() {
        super();
    }
}
