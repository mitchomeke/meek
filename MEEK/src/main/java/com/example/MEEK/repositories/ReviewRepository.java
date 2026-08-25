package com.example.MEEK.repositories;

import com.example.MEEK.resources.Music;
import com.example.MEEK.resources.Review;
import com.example.MEEK.resources.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByUserIn(List<User> friends);

    @Query("SELECT r.music from Review as r where r.user = :user")
    List<Music> songOfReviewsByUser(@Param("user") User user);

    @Query("SELECT r from Review r where r.user = :user and r.music = :music")
    Review getReviewOfMusic(@Param("user") User user, @Param("music") Music music);

    @Query("select sum(r.rating)/count(r) from Review r where r.music = :music")
    Double getMusicRating(@Param("music")Music music);

    @Query("select r from Review r where r.user = :user")
    List<Review> getUserReviews(User user);

}
