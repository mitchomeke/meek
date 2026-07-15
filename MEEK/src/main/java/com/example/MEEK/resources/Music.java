package com.example.MEEK.resources;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Music {
    @Id
    @GeneratedValue private Long id;
    protected String musicName;
    protected Date releaseDate;
    protected String artistName;
    protected int musicLength;

    @OneToMany(mappedBy = "music", cascade = CascadeType.ALL)
    List<Review> reviews = new ArrayList<>();

    public Music(){}
    public Music(String musicName, Date releaseDate, String artistName, int musicLength){
        this.artistName = artistName;
        this.musicName = musicName;
        this.releaseDate = releaseDate;
        this.musicLength = musicLength;
    }
    public Music(String musicName, Date releaseDate, String artistName){
        this.artistName = artistName;
        this.musicName = musicName;
        this.releaseDate = releaseDate;
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

    public double getMusicRating(){
        double sum = 0;
        for (Review reviews : reviews){
            sum = sum + reviews.getRating();
        }
        return sum/reviews.size();
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
