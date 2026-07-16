package com.example.MEEK.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.util.ArrayList;
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
    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<User> meekers = new ArrayList<>();

    @Lob
    @Column(name = "display_photo", columnDefinition = "LONGBLOB")
    @JsonIgnore
    private byte[] displayPhoto;

    public User(){}
    public User(String userName, byte[] displayPhoto){
        this.userName = userName;
        this.displayPhoto = displayPhoto;
    }
    public User(String userName){
        this.userName = userName;
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
    public List<String> getMeekerNames(){
        if (getMeekers() == null){
            return List.of();
        }
        return getMeekers().stream().map(
                User::getUserName
        ).toList();
    }
    public void addMeeker(User user){
        meekers.add(user);
    }

    public Long getId() {
        return id;
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
