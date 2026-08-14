package com.example.MEEK.resources;

import jakarta.persistence.*;

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
}
