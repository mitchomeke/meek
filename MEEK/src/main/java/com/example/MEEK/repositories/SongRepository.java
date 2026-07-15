package com.example.MEEK.repositories;

import com.example.MEEK.resources.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song,Long> {
}
