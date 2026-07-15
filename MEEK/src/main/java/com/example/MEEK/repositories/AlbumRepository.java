package com.example.MEEK.repositories;


import com.example.MEEK.resources.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album,Long> {
}
