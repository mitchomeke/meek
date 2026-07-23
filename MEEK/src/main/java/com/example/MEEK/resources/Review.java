package com.example.MEEK.resources;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Reviews")
public class Review {
    @Id
    @GeneratedValue private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_id")
    private Music music;

    private double rating;
    private String description;
    private String userName;

    public Review(){}
    public Review(String userName, Music music, double rating, String description){
        this.userName = userName;
        this.music = music;
        this.rating = rating;
        this.description = description;
    }
    public Review(String userName, double rating, String description){
        this.userName = userName;
        this.rating = rating;
        this.description = description;
    }
    public Review(User user, Music music, double rating, String description){
        this.user = user;
        this.music = music;
        this.rating = rating;
        this.description = description;
        this.userName = user.getUserName();
        user.addReview(this);
        music.addReview(this);
    }

    public Long getId() {
        return id;
    }

    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Music getMusic() {
        return music;
    }
    public String getMusicName(){
        return music.getMusicName();
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Double.compare(rating, review.rating) == 0 && Objects.equals(id, review.id) && Objects.equals(user, review.user) && Objects.equals(music, review.music);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, music, rating);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", user=" + user +
                ", music=" + music +
                ", rating=" + rating +
                '}';
    }
}
