package com.example.MEEK.repositories;

import com.example.MEEK.resources.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music,Long> {
}
