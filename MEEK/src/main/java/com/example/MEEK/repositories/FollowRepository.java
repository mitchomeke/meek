package com.example.MEEK.repositories;

import com.example.MEEK.resources.Follow;
import com.example.MEEK.resources.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    @Query("SELECT f.receiver FROM Follow f where f.sender = :user")
    List<User> getReceiversFor(@Param("user") User user);

    @Query("select f from Follow f where f.sender = :sender and f.receiver = :receiver")
    Optional<Follow> findBetweenSenderAndReceiver(User sender, User receiver);

    @Query("select f from Follow f where (f.sender = :firstUser and f.receiver = :secondUser) or " +
            "(f.sender = :secondUser and f.receiver = :firstUser)")
    List<Optional<Follow>> findBetweenUsers(User firstUser, User secondUser);
}
