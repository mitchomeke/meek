package com.example.MEEK.assemblers;

import com.example.MEEK.controllers.MusicRestController;
import com.example.MEEK.resources.Music;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class MusicAssembler implements RepresentationModelAssembler<Music, EntityModel<Music>> {
    @Override
    public EntityModel<Music> toModel(Music music){
        return EntityModel.of(music,linkTo(methodOn(MusicRestController.class).getAllSongs()).withRel("music"),
                linkTo(methodOn(MusicRestController.class).getSong(music.getId())).withSelfRel());
    }
}
