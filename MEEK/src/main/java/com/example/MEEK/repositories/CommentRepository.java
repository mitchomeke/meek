package com.example.MEEK.repositories;

import com.example.MEEK.resources.Comment;
import com.example.MEEK.resources.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("SELECT c from Comment c where c.review = :review")
    List<Comment> findAllCommentsOfReview(@Param("review") Review review);
}
