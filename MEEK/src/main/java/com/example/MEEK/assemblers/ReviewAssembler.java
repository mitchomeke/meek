package com.example.MEEK.assemblers;

import com.example.MEEK.resources.Review;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ReviewAssembler implements RepresentationModelAssembler<Review, EntityModel<Review>> {
    @Override
    public EntityModel<Review> toModel(Review review) {
        return EntityModel.of(review);
    }
}
