package com.example.MEEK.assemblers;

import com.example.MEEK.resources.Music;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class MusicAssembler implements RepresentationModelAssembler<Music, EntityModel<Music>> {
    @Override
    public EntityModel<Music> toModel(Music music){

        return null;
    }
}
