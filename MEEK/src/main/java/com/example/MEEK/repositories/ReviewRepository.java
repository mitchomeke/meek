package com.example.MEEK.repositories;

import com.example.MEEK.resources.Review;
import com.example.MEEK.resources.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByUserIn(List<User> friends);

}
