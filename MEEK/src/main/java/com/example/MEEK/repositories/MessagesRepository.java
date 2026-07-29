package com.example.MEEK.repositories;

import com.example.MEEK.resources.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessagesRepository extends JpaRepository<Message,Long> {
}
