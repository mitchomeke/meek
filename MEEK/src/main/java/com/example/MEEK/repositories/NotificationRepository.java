package com.example.MEEK.repositories;

import com.example.MEEK.resources.Notification;
import com.example.MEEK.resources.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    public static final String MESSAGE = "MESSAGE";
    public static final String FOLLOW = "FOLLOW";
    public static final String LIKE = "LIKE";
    public static final String COMMENT = "COMMENT";
    List<Notification> findByReceiver(User user);
    @Modifying
    @Transactional
    @Query("delete from Notification n where n.receiver = :user or n.sender = :user")
    void deleteAllByUser(@Param("user") User user);
}
