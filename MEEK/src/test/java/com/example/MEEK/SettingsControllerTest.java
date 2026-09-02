package com.example.MEEK;

import com.example.MEEK.config.SecurityConfig;
import com.example.MEEK.controllers.SettingsController;
import com.example.MEEK.repositories.UserRepository;
import com.example.MEEK.resources.User;
import com.example.MEEK.services.CustomUserDetailsService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
// --- Static Imports for MockMvc Request Building ---
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

// --- Static Imports for Result Matchers & Logging ---
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

// --- Static Imports for Spring Security Test Utilities ---
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

// --- Static Imports for Mockito Stubbing ---
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static reactor.core.Disposables.never;

@WebMvcTest(SettingsController.class)
@Import(SecurityConfig.class)
public class SettingsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(username = "mitch_31")
    void updateBio_whenBioExceeds150Chars_shouldRedirectWithTooLongError() throws Exception{
        String longBio = "a".repeat(151);
        User mockUser = new User();
        mockUser.setUserName("mitch_31");
        when(userRepository.findByUserName("mitch_31")).thenReturn(Optional.of(mockUser));
        mockMvc.perform(post("/settings/changeBio")
                .param("newBio",longBio)
                        .with(user("mitch_31"))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings?bioTooLong"));
    }

    @Test
    @WithMockUser(username = "mitch_31")
    void updateBioWorking() throws Exception {
        String normalBio = "newBioHuh";
        User mockUser = new User();
        mockUser.setUserName("mitch_31");
        when(userRepository.findByUserName("mitch_31")).thenReturn(Optional.of(mockUser));

        mockMvc.perform(post("/settings/changeBio")
                .param("newBio",normalBio)
                        .with(user("mitch_31"))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings?bioUpdated"));

        verify(userRepository).save(mockUser);
    }
    @Test
    @WithMockUser(username = "mitch_31")
    void currentPasswordWrong() throws Exception {
        String currentPassword = "LMAO";
        String newPassword = "LMAO";
        String finalPassword = "LMAO";

        User mockUser = new User();
        mockUser.setUserName("mitch_31");
        mockUser.setPassword("LMFAO");
        when(userRepository.findByUserName("mitch_31")).thenReturn(Optional.of(mockUser));

        mockMvc.perform(post("/settings/changePassword")
                .param("currentPassword",currentPassword)
                .param("newPassword",newPassword)
                .param("finalPassword",finalPassword)
                .with(user("mitch_31"))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings?passwordWrong"));
    }
    @Test
    @WithMockUser(username = "mitch_31")
    void passWordDontMatch() throws Exception {
        User mockUser = new User();
        mockUser.setUserName("mitch_31");
        mockUser.setPassword("encodedHash");
        when(userRepository.findByUserName("mitch_31")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("currentPassword","encodedHash")).thenReturn(true);

        mockMvc.perform(post("/settings/changePassword")
                .param("currentPassword","currentPassword")
                .param("newPassword","newPassword")
                .param("finalPassword","finalPassword")
                .with(user("mitch_31"))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings?passwordDontMatch"));

        verify(passwordEncoder).matches("currentPassword","encodedHash");
    }
    @Test
    @WithMockUser(username = "mitch_31")
    void passWordSameAsBefore() throws Exception {
        User mockUser = new User();
        mockUser.setUserName("mitch_31");
        mockUser.setPassword("currentPassword");
        when(userRepository.findByUserName("mitch_31")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("encodedPass","currentPassword")).thenReturn(true);

        mockMvc.perform(post("/settings/changePassword")
                .param("currentPassword","encodedPass")
                .param("newPassword","encodedPass")
                .param("finalPassword","encodedPass")
                .with(user("mitch_31"))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings?sameAsBefore"));
    }
    @Test
    @WithMockUser(username = "mitch_31")
    void passwordWorking() throws Exception {
        String currentPassword = "current";
        String newPassword = "final";
        String finalPassword = "final";
        User mockUser = new User();
        mockUser.setUserName("mitch_31");
        mockUser.setPassword(currentPassword);
        when(userRepository.findByUserName("mitch_31")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(currentPassword,"current")).thenReturn(true);

        mockMvc.perform(post("/settings/changePassword")
                .param("currentPassword",currentPassword)
                .param("newPassword",newPassword)
                .param("finalPassword",finalPassword)
                .with(user("mitch_31"))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings?passwordUpdated"));

        verify(userRepository).save(mockUser);
    }
}
