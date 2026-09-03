package com.example.MEEK.controllers;

import com.example.MEEK.repositories.UserRepository;
import com.example.MEEK.resources.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/searchUsers")
public class UserSearchController {
    private final UserRepository userRepository;

    public UserSearchController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getSearchUsersPage(Model model, @RequestParam(required = false,name = "query") String query,
                                     Principal principal){
        searchUsersMethod(model,query,principal);
        return "searchUsers";
    }
    private void searchUsersMethod(Model model, String query, Principal principal){
        if (query != null && !query.isEmpty()){
            List<User> usersResponse = new ArrayList<>(userRepository.searchUsers(query).stream().toList());
            User loggedInUser = userRepository.findByUserName(principal.getName()).orElseThrow();
            model.addAttribute("loggedInUser",loggedInUser);
            usersResponse.remove(loggedInUser);
            if (!usersResponse.isEmpty()){
                model.addAttribute("allUsers",usersResponse);
            }
            model.addAttribute("query",query);
        }
    }
}
