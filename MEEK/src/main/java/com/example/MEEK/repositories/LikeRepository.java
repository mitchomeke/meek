package com.example.MEEK.repositories;

import com.example.MEEK.resources.Like;
import com.example.MEEK.resources.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like,Long> {
    List<Like> findByReceiver(User user);
    List<Like> findBySender(User user);
}
