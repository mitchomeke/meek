package com.example.MEEK;

import com.example.MEEK.repositories.*;
import com.example.MEEK.resources.*;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Configuration
public class LoadDatabase {
    private Logger log = Logger.getLogger(LoadDatabase.class.getName());
    public LoadDatabase(){}

    @Bean
    @Transactional
    CommandLineRunner initDatabase(AlbumRepository albumRepository,
                                   SongRepository songRepository,
                                   UserRepository userRepository,
                                   MusicRepository musicRepository,
                                   ReviewRepository reviewRepository,
                                   NotificationRepository notificationRepository,
                                   PasswordEncoder passwordEncoder) throws IOException {

        ClassPathResource resource = new ClassPathResource("images/xperiment.png");
        byte[] photoBytes = resource.getContentAsByteArray();
        String encodedPassword = passwordEncoder.encode("Mitchell"); // All users share this password

        return args -> {
            // ==========================================
            // 1. CREATE USERS
            // ==========================================
            User mitch = userRepository.save(new User("mitch_31", photoBytes, encodedPassword));
            User angela = userRepository.save(new User("angela_26", photoBytes, encodedPassword));
            User daisy = userRepository.save(new User("daisy_44", photoBytes, encodedPassword));
            User alex = userRepository.save(new User("alex_beats", photoBytes, encodedPassword));
            User sarah = userRepository.save(new User("sarah_vibes", photoBytes, encodedPassword));

            // Set bios for each user
            mitch.setBio("Coffee addict ☕ | Running enthusiast 🏃 | Always looking for new food spots");
            angela.setBio("UI/UX Designer by day, amateur baker by night 🍰✨");
            daisy.setBio("Plant mom 🌿 | Books, travel, & good wine 🍷");
            alex.setBio("Music producer & vinyl collector 🎧🎹 Drop your playlist recs below!");
            sarah.setBio("Living for the weekend 🌊 | Photography & outdoor adventures 📸");


            userRepository.findAll().forEach(user -> log.info("Preloaded User -> " + user.getUserName()));

            // Make everyone friends with each other
            List<User> allUsers = userRepository.findAll();
            for (User user : allUsers) {
                for (User other : allUsers) {
                    if (!user.getId().equals(other.getId())) {
                        Follow follow = new Follow(user,other);
                        notificationRepository.save(follow);
                    }
                }
                userRepository.save(user);
            }

            // ==========================================
            // 2. CREATE SONGS & ALBUMS
            // ==========================================

            // Single Track (Standalone)
            Song cocaineNose = new Song("COCAINE NOSE", LocalDate.of(2025, 1, 6), "Playboi Carti", 4);
            songRepository.save(cocaineNose);

            // --- Album 1: Blush by Kevin Abstract ---
            Song popOut = new Song("Pop Out", LocalDate.of(2025, 1, 2), "Kevin Abstract", 3);
            Song copy = new Song("COPY", LocalDate.of(2025, 1, 2), "Kevin Abstract", 2);
            Song nola = new Song("NOLA", LocalDate.of(2025, 1, 2), "Kevin Abstract", 3);

            List<Song> blushTracks = List.of(popOut, copy, nola);
            Album blush = new Album("Blush", LocalDate.of(2025, 1, 2), "Kevin Abstract", blushTracks);
            for (Song song : blushTracks) {
                song.setAlbum(blush);
            }
            albumRepository.save(blush); // Cascades and saves tracks if configured, or save tracks explicitly

            // --- Album 2: Chromakopia by Tyler, The Creator ---
            Song stChroma = new Song("St. Chroma", LocalDate.of(2024, 10, 28), "Tyler, The Creator", 4);
            Song rahTahTah = new Song("Rah Tah Tah", LocalDate.of(2024, 10, 28), "Tyler, The Creator", 3);
            Song darlingI = new Song("Darling, I", LocalDate.of(2024, 10, 28), "Tyler, The Creator", 4);
            Song sticky = new Song("Sticky", LocalDate.of(2024, 10, 28), "Tyler, The Creator", 5);

            List<Song> chromaTracks = List.of(stChroma, rahTahTah, darlingI, sticky);
            Album chromakopia = new Album("Chromakopia", LocalDate.of(2024, 10, 28), "Tyler, The Creator", chromaTracks);
            for (Song song : chromaTracks) {
                song.setAlbum(chromakopia);
            }
            albumRepository.save(chromakopia);

            // --- Album 3: Hit Me Hard and Soft by Billie Eilish ---
            Song lunch = new Song("LUNCH", LocalDate.of(2024, 5, 17), "Billie Eilish", 3);
            Song chihiro = new Song("CHIHIRO", LocalDate.of(2024, 5, 17), "Billie Eilish", 5);
            Song birdsOfAFeather = new Song("BIRDS OF A FEATHER", LocalDate.of(2024, 5, 17), "Billie Eilish", 4);

            List<Song> billieTracks = List.of(lunch, chihiro, birdsOfAFeather);
            Album hitMeHard = new Album("Hit Me Hard and Soft", LocalDate.of(2024, 5, 17), "Billie Eilish", billieTracks);
            for (Song song : billieTracks) {
                song.setAlbum(hitMeHard);
            }
            albumRepository.save(hitMeHard);

            // ==========================================
            // 3. CREATE REVIEWS & LIKES
            // ==========================================

            // Review 1: Mitch reviews "CHIHIRO"
            Review r1 = new Review(mitch, chihiro, 5, "Absolute masterpiece! Production on this track is insane.");
            reviewRepository.save(r1);

            // Review 2: Daisy reviews "Pop Out"
            Review r2 = new Review(daisy, popOut, 4, "Kevin Abstract never fails to deliver a solid hook.");
            reviewRepository.save(r2);

            // Review 3: Angela reviews "Sticky"
            Review r3 = new Review(angela, sticky, 5, "Best feature line-up on any track this year!");
            reviewRepository.save(r3);

            // Review 4: Alex reviews "COCAINE NOSE"
            Review r4 = new Review(alex, cocaineNose, 3, "Fun track, but feeling a bit repetitive after a few listens.");
            reviewRepository.save(r4);

            // Add some initial likes between users
            // Mitch likes Angela's review
            notificationRepository.save(new Like(mitch,angela,r3, Instant.now()));
            // Daisy likes Mitch's review
            notificationRepository.save(new Like(daisy,mitch,r1,Instant.now()));
            // Sarah likes Mitch's review
            notificationRepository.save(new Like(sarah,mitch,r1,Instant.now()));

            userRepository.save(mitch);
            userRepository.save(daisy);
            userRepository.save(sarah);
            userRepository.save(angela);

            log.info("Database Initialization Complete!");
        };
    }
}
