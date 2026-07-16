package com.example.MEEK.assemblers;

import com.example.MEEK.controllers.UserRestController;
import com.example.MEEK.resources.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Override
    public EntityModel<User> toModel(User user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserRestController.class).getUser(user.getId())).withSelfRel(),
                linkTo(methodOn(UserRestController.class).getPhoto(user.getId())).withRel("photo"),
                linkTo(methodOn(UserRestController.class).getMeekers(user.getId())).withRel("meekers"));
    }
}
