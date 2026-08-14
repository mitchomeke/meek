package com.example.MEEK.repositories;

import com.example.MEEK.resources.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
