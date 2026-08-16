package com.example.MEEK.resources;

import com.example.MEEK.repositories.NotificationRepository;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
public abstract class Notification {
    @Id
    @GeneratedValue
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    protected User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    protected User receiver;

    protected boolean isDismissed = false;

    protected Instant exactTime;

    public Notification() {}

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
    public Long getId() {
        return id;
    }
    public boolean isMessage(){
        return this.getClass() == Message.class;
    }

    public boolean isDismissed() {
        return isDismissed;
    }

    public void setDismissed(boolean dismissed) {
        isDismissed = dismissed;
    }

    public Instant getExactTime() {
        return exactTime;
    }

    public void setExactTime(Instant exactTime) {
        this.exactTime = exactTime;
    }
    public String notificationType(){
        if (this.getClass() == Message.class){
            return NotificationRepository.MESSAGE;
        }
        else if (this.getClass() == Like.class){
            return NotificationRepository.LIKE;
        }
        else {
            return NotificationRepository.FOLLOW;
        }
    }
}
