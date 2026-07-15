package com.example.MEEK.resources;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue private Long id;
    private String userName;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> meekers;

    @Lob
    @Column(name = "display_photo", columnDefinition = "LONGBLOB")
    private byte[] displayPhoto;

    public User(){}
    public User(String userName, byte[] displayPhoto){
        this.userName = userName;
        this.displayPhoto = displayPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] getDisplayPhoto() {
        return displayPhoto;
    }

    public void setDisplayPhoto(byte[] displayPhoto) {
        this.displayPhoto = displayPhoto;
    }

    public List<User> getMeekers() {
        return meekers;
    }
    public void addMeeker(User user){
        meekers.add(user);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(userName, user.userName) && Objects.deepEquals(displayPhoto, user.displayPhoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, Arrays.hashCode(displayPhoto));
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", displayPhoto=" + Arrays.toString(displayPhoto) +
                '}';
    }
}
