package com.example.mysqlstuff.objects;

import java.util.Date;

public class User {

    /**
     * Declaring Vars for the User Object
     * @param username
     * @param sessionExpiryDate
     * @param bio
     * @param profilePic_url
     * @param followers
     * @param userId
     */

    String username;
    Date sessionExpiryDate;
    String bio;
    String profilePic_url;
    int followers;
    int userId;

    /**
     * Default constructor for the User Object
     */

    public User(){
    }

    /**
     * Constructor for the User Object
     * @param username
     * @param sessionExpiryDate
     * @param bio
     * @param profilePic_url
     * @param followers
     * @param userId
     */

    public User(String username, Date sessionExpiryDate, String bio, String profilePic_url, int followers, int userId) {
        this.username = username;
        this.sessionExpiryDate = sessionExpiryDate;
        this.bio = bio;
        this.profilePic_url = profilePic_url;
        this.followers = followers;
        this.userId = userId;
    }

    /**
     * Getters and Setters for the User Object 
     */

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getUsername() {
        return username;
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
