package com.example.MEEK.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class Song extends Music {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnore
    private Album album;

    public Song(){}
    public Song(String musicName, LocalDate releaseDate, String artistName, int musicLength){
        super(musicName,releaseDate,artistName,musicLength);
    }
    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getAlbumRating(){
        if (album == null){
            return "This song is a single.";
        }
        return "This song's album has a rating of: "+ album.getMusicRating();
    }
}
