package com.example.MEEK.repositories;

import com.example.MEEK.resources.Message;
import com.example.MEEK.resources.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessagesRepository extends JpaRepository<Message,Long> {

    @Query("SELECT m from Message m where m.receiver = :user or m.sender =: user order by m.exactTime desc")
    List<Message> getUserInbox(@Param("user") User user);
}
