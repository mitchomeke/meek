package com.example.MEEK.repositories;

import com.example.MEEK.resources.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music,Long> {
    Music getMusicByMusicNameAndArtistName(String musicName, String artistName);
}
