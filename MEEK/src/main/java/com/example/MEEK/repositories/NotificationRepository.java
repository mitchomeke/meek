package com.example.MEEK.repositories;

import com.example.MEEK.resources.Notification;
import com.example.MEEK.resources.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    public static final String MESSAGE = "MESSAGE";
    public static final String FOLLOW = "FOLLOW";
    public static final String LIKE = "LIKE";
    List<Notification> findByReceiver(User user);
}
