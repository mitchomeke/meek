package com.example.MEEK.repositories;

import com.example.MEEK.resources.Like;
import com.example.MEEK.resources.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {
    List<Like> findByReceiver(User user);
    List<Like> findBySender(User user);
    Optional<Like> findByReviewAndSender(Review review, User user);
    @Query("select l.review from Like l where l = :like")
    Review getReviewOfLike(@Param("like") Like like);
    @Transactional
    List<Like> deleteLikesBySender(User user);
    @Modifying
    @Transactional
    @Query("delete from Like l where l.review = :review")
    void deleteLikesOfReview(@Param("review") Review review);
}
