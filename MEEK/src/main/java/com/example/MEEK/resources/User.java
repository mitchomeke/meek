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
    private String firstName;
    private String lastName;
    private String userName;
    private String fullName;
    @Lob
    @Column(name = "display_photo", columnDefinition = "LONGBLOB")
    @JsonIgnore
    private String displayPhoto;
    private String encryptedPassword;
    private String bio;
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<User> blockedUsers = new ArrayList<>();

    public User(){}
    public User(String userName, String displayPhoto, String encryptedPassword){
        this.userName = userName;
        this.displayPhoto = displayPhoto;
        this.encryptedPassword = encryptedPassword;
    }
    public User(String userName){
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }
    public String getDisplayPhoto() {
        return displayPhoto;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setDisplayPhoto(String displayPhoto) {
        this.displayPhoto = displayPhoto;
    }
    public Long getId() {
        return id;
    }
    public void setPassword(String password){
        encryptedPassword = password;
    }
    public String getEncryptedPassword() {
        return encryptedPassword;
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getFullName(){
      return firstName + " " + lastName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public List<User> getBlockedUsers() {
        return blockedUsers;
    }
    public void setBlockedUsers(List<User> blockedUsers) {
        this.blockedUsers = blockedUsers;
    }
    public void blockUser(User user){
        blockedUsers.add(user);
    }
    public void unblockUser(User user){
        blockedUsers.remove(user);
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(userName, user.userName) && Objects.deepEquals(displayPhoto, user.displayPhoto);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, userName, Arrays.hashCode(displayPhoto.toCharArray()));
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", displayPhoto=" + displayPhoto +
                '}';
    }
}
