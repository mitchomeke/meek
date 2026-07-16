package com.example.MEEK.controllers;
import com.example.MEEK.*;
import com.example.MEEK.assemblers.MusicAssembler;
import com.example.MEEK.exceptions.SongNotFound;
import com.example.MEEK.repositories.AlbumRepository;
import com.example.MEEK.repositories.SongRepository;
import com.example.MEEK.resources.Music;
import com.example.MEEK.resources.Song;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class MusicRestController {
    private MusicAssembler musicAssembler;
    private SongRepository songRepository;
    private AlbumRepository albumRepository;

    public MusicRestController(MusicAssembler musicAssembler, SongRepository songRepository,
                               AlbumRepository albumRepository){
        this.musicAssembler = musicAssembler;
        this.songRepository = songRepository;
        this.albumRepository = albumRepository;
    }

    @GetMapping("/music/songs")
    public CollectionModel<EntityModel<Music>> getAllSongs(){
        List<EntityModel<Music>> singles = songRepository.findAll().stream().map(
                song -> musicAssembler.toModel(song)
        ).toList();
        return CollectionModel.of(singles,linkTo(methodOn(MusicRestController.class).
                getAllSongs()).withRel("allSongs"));
    }

    @GetMapping("/music/songs/{id}")
    public EntityModel<Music> getSong(@PathVariable Long id){
        return songRepository.findById(id).map(
                song -> musicAssembler.toModel(song)
        ).orElseThrow(
                () -> new SongNotFound(id)
        );
    }
    @GetMapping("/music/singles")
    public CollectionModel<EntityModel<Music>> getAllSingles(){
        List<EntityModel<Music>> singles = songRepository.findAll().stream().filter(
                song -> song.getAlbum() == null
        ).map(
                song -> musicAssembler.toModel(song)
        ).toList();
        return CollectionModel.of(singles,linkTo(methodOn(MusicRestController.class).
                getAllSongs()).withRel("allSingles"));
    }

    @GetMapping("/music/singles/{id}")
    public EntityModel<Music> getSingle(@PathVariable Long id){
        return songRepository.findById(id).filter(
                song -> song.getAlbum() == null
        ).map(
                song -> musicAssembler.toModel(song)
        ).orElseThrow(
                () -> new SongNotFound(id)
        );
    }
}
