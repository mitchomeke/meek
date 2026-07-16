package com.example.MEEK;

import com.example.MEEK.repositories.AlbumRepository;
import com.example.MEEK.repositories.MusicRepository;
import com.example.MEEK.repositories.SongRepository;
import com.example.MEEK.repositories.UserRepository;
import com.example.MEEK.resources.Album;
import com.example.MEEK.resources.Song;
import com.example.MEEK.resources.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    CommandLineRunner initDatabase(AlbumRepository albumRepository, SongRepository songRepository,
                                   UserRepository userRepository, MusicRepository musicRepository) throws IOException {
        Path photoPath = Paths.get("C:/Users/PC/Downloads/xperiment.png");
        byte[] photoBytes = Files.readAllBytes(photoPath);
        return args -> {
            log.info("User Created -> "+ userRepository.save(new User("mitch_31",photoBytes)));
            log.info("User Created -> "+ userRepository.save(new User("angela_26",photoBytes)));
            log.info("User Created -> "+ userRepository.save(new User("daisy_44",photoBytes)));

            userRepository.findAll().forEach(
                    user -> log.info("Preloaded -> "+ user)
            );
            Song cocaineNose = new Song("COCAINE NOSE",LocalDate.of(2025,1,6),"Playboi Carti",4);
            Song popOut = new Song("Pop Out",LocalDate.of(2025,1,2),"Kevin Abstract",3);
            Song copy = new Song("COPY", LocalDate.of(2025,1,2),"Kevin Abstract",2);
            Song nola = new Song("NOLA",LocalDate.of(2025,1,2),"Kevin Abstract",3);

            List<Song> tracks = new ArrayList<>();
            tracks.add(popOut); tracks.add(copy); tracks.add(nola);

            Album blush = new Album("Blush",
                    LocalDate.of(2025,1,2),"Kevin Abstract",tracks);

            for (Song song : tracks){
                song.setAlbum(blush);
            }

            log.info("Song Created -> "+ musicRepository.save(cocaineNose));
            songRepository.findAll().forEach(
                    song -> log.info("Preloaded -> " + song)
            );

            log.info("Album Created -> " + musicRepository.save(blush));
            albumRepository.findAll().forEach(
                    album -> log.info("Preloaded -> " + album)
            );
        };
    }
}
