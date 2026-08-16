package com.example.MEEK.repositories;

import com.example.MEEK.resources.Follow;
import com.example.MEEK.resources.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    @Query("SELECT f.receiver FROM Follow f where f.sender = :user")
    List<User> getReceiversFor(@Param("user") User user);
}
