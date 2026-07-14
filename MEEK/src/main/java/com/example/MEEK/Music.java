package com.example.MEEK;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.Objects;

@Entity
public abstract class Music {
    @Id
    @GeneratedValue private Long id;
    private String musicName;
    private Date releaseDate;
    private String artistName;
    private int musicLength;

    public Music(){}
    public Music(String musicName, Date releaseDate, String artistName, int musicLength){
        this.artistName = artistName;
        this.musicName = musicName;
        this.releaseDate = releaseDate;
        this.musicLength = musicLength;
    }

    public Long getId() {
        return id;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getMusicLength() {
        return musicLength;
    }

    public void setMusicLength(int musicLength) {
        this.musicLength = musicLength;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Music music = (Music) o;
        return musicLength == music.musicLength && Objects.equals(id, music.id) && Objects.equals(musicName, music.musicName) && Objects.equals(releaseDate, music.releaseDate) && Objects.equals(artistName, music.artistName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, musicName, releaseDate, artistName, musicLength);
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", musicName='" + musicName + '\'' +
                ", releaseDate=" + releaseDate +
                ", artistName='" + artistName + '\'' +
                ", musicLength=" + musicLength +
                '}';
    }
}
