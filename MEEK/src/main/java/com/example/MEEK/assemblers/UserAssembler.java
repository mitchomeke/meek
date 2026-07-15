package com.example.MEEK.assemblers;

import com.example.MEEK.resources.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class UserAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Override
    public EntityModel<User> toModel(User entity) {
        return null;
    }
}
