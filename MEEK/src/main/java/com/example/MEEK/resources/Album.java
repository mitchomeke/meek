package com.example.MEEK.resources;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Album extends Music {
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<Song> tracks = new ArrayList<>();

    public Album(){}
    public Album(String musicName, LocalDate releaseDate, String artistName, List<Song> tracks){
        super(musicName, releaseDate, artistName);
        this.setTracks(tracks);
        setMusicLength();
    }

    public List<Song> getTracks() {
        return tracks;
    }

    public void setTracks(List<Song> tracks) {
        this.tracks = tracks;
    }

    public void setMusicLength(){
        for (Song song : tracks){
         this.musicLength = this.musicLength + song.getMusicLength();
        }
    }
    @Override
    public double getRating(){
        double sum = 0;
        int count = 0;
        for (Song song : tracks) {
            for (Review review : song.reviews) {
                sum += review.getRating();
                count++;
            }
        }
        for (Review review : reviews) {
            sum += review.getRating();
            count++;
        }
        if (count == 0) return 0.0;
        return Math.round((sum / count) * 10.0) / 10.0;
    }

}
