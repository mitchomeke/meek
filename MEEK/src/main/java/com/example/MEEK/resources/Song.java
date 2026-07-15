package com.example.MEEK.resources;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Song extends Music {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    public Song(){}
    public Song(String musicName, Date releaseDate, String artistName, int musicLength){
        super(musicName,releaseDate,artistName,musicLength);
    }
    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public double getAlbumRating(){
        return album.getMusicRating();
    }
}
