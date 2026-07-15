package com.example.MEEK.repositories;

import com.example.MEEK.resources.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
