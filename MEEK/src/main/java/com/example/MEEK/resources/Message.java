package com.example.MEEK.resources;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Message extends Notification {

    @Column(length = 1000)
    private String content;

    private LocalDateTime timestamp = LocalDateTime.now();

    public Message(User sender, User receiver, String content, LocalDateTime timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
    }
    public Message(){}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
