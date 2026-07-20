package com.example.MEEK.services;

import com.example.MEEK.exceptions.UserNotFound;
import com.example.MEEK.repositories.UserRepository;
import com.example.MEEK.resources.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).orElseThrow(
                () -> new UserNotFound(1L)
        );
        return new org.springframework.security.core.userdetails.User(user.getUserName(),user.getEncryptedPassword(), Collections.emptyList());
    }
}
