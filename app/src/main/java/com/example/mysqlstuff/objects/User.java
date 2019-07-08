package com.example.mysqlstuff.objects;

import java.util.Date;

public class User {

    String username;
    String email;
    Date sessionExpiryDate;
    String bio;
    String profilePic_url;
    int followers;
    int userId;

    public User(){
    }

    public User(String username, String email, Date sessionExpiryDate, String bio, String profilePic_url, int followers, int userId) {
        this.username = username;
        this.email = email;
        this.sessionExpiryDate = sessionExpiryDate;
        this.bio = bio;
        this.profilePic_url = profilePic_url;
        this.followers = followers;
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePic_url() {
        return profilePic_url;
    }

    public void setProfilePic_url(String profilePic_url) {
        this.profilePic_url = profilePic_url;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
