package com.example.MEEK.repositories;

import com.example.MEEK.resources.Message;
import com.example.MEEK.resources.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessagesRepository extends JpaRepository<Message,Long> {
}
