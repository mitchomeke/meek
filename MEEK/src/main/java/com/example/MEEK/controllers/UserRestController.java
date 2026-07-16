package com.example.MEEK.controllers;
import com.example.MEEK.assemblers.UserAssembler;
import com.example.MEEK.exceptions.UserNotFound;
import com.example.MEEK.repositories.UserRepository;
import com.example.MEEK.resources.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class UserRestController {
    private UserAssembler userAssembler;
    private UserRepository userRepository;

    public UserRestController(UserAssembler userAssembler, UserRepository userRepository){
        this.userAssembler = userAssembler;
        this.userRepository = userRepository;
    }
    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> getAllUsers(){
        List<EntityModel<User>> users = userRepository.findAll().stream().map(
                user -> userAssembler.toModel(user)
        ).toList();
        return CollectionModel.of(users,linkTo(methodOn(UserRestController.class).getAllUsers()).withSelfRel());
    }
    @GetMapping("/users/{id}")
    public EntityModel<User> getUser(@PathVariable Long id){
        return userRepository.findById(id).map(
                user -> userAssembler.toModel(user)
        ).orElseThrow(
                () -> new UserNotFound(id)
        );
    }
    @GetMapping(value = "/users/{id}/photo", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long id){
        byte[] userPhoto = userRepository.findById(id).map(
                user -> Objects.requireNonNull(user.getDisplayPhoto())
        ).orElseThrow(
                () -> new UserNotFound(id)
        );
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(userPhoto);
    }
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@ModelAttribute User user, @RequestParam("file") MultipartFile file){
        try {
            if (!file.isEmpty()){
                user.setDisplayPhoto(file.getBytes());
            }
            User existsUser = getUserByUserName(user.getUserName());
            if (existsUser != null){
                return ResponseEntity.internalServerError().body("User with this username already exists.");
            }
            EntityModel<User> createdUser = userAssembler.toModel(userRepository.save(user));
            return ResponseEntity.created(createdUser.getRequiredLink(IanaLinkRelations.SELF).toUri()).build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error Uploading Image.");
        }
    }
    @PutMapping("/users/follow/{id}")
    public ResponseEntity<?> followUser(@PathVariable Long id, @RequestParam String userName){
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFound(id)
        );
        if (user.getUserName().equalsIgnoreCase(userName)){
            return ResponseEntity.internalServerError().body("User cannot follow himself/herself");
        }
        User existUser = getUserByUserName(userName);
        if (existUser == null){
            return ResponseEntity.internalServerError().body("The User you want to follow does not exist");
        }
        user.addMeeker(existUser);
        EntityModel<User> userModel = userAssembler.toModel(userRepository.save(user));
        return ResponseEntity.created(userModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).build();
    }
    @GetMapping("/users/meekers/{id}")
    public CollectionModel<EntityModel<User>> getMeekers(@PathVariable Long id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFound(id)
        );
        List<EntityModel<User>> meekers = user.getMeekers().stream().map(
                user1 -> userAssembler.toModel(user1)
        ).collect(Collectors.toList());
        return CollectionModel.of(meekers,linkTo(methodOn(UserRestController.class).getMeekers(id)).withRel("meekers"));
    }
    private User getUserByUserName(String userName){
        List<User> ExistUser = userRepository.findAll().stream().filter(
                user1 -> user1.getUserName().equalsIgnoreCase(userName.trim())
        ).toList();
        return ExistUser.getFirst();
    }
}
