package com.example.mysqlstuff.model;

import java.util.Date;

public class otherUser {

    private int otherUserId;
    private String otherUsername;
    private String otherEmail;
    private String otherName;
    private String otherLocation;
    private String otherBio;
    private String otherPronoun;
    private String otherProfilePic_url;
    private int otherFollowers;

    public otherUser() {
    }

    public otherUser(int otherUserID, String otherUsername, String otherEmail, String otherName, String otherLocation, String otherBio, String otherPronoun, String otherProfilePic_url, int otherFollowers) {
        this.otherUserId = otherUserID;
        this.otherUsername = otherUsername;
        this.otherEmail = otherEmail;
        this.otherName = otherName;
        this.otherLocation = otherLocation;
        this.otherBio = otherBio;
        this.otherPronoun = otherPronoun;
        this.otherProfilePic_url = otherProfilePic_url;
        this.otherFollowers = otherFollowers;
    }

    public int getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(int otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getOtherUsername() {
        return otherUsername;
    }

    public void setOtherUsername(String otherUsername) {
        this.otherUsername = otherUsername;
    }

    public String getOtherEmail() {
        return otherEmail;
    }

    public void setOtherEmail(String otherEmail) {
        this.otherEmail = otherEmail;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getOtherLocation() {
        return otherLocation;
    }

    public void setOtherLocation(String otherLocation) {
        this.otherLocation = otherLocation;
    }

    public String getOtherBio() {
        return otherBio;
    }

    public void setOtherBio(String otherBio) {
        this.otherBio = otherBio;
    }

    public String getOtherPronoun() {
        return otherPronoun;
    }

    public void setOtherPronoun(String otherPronoun) {
        this.otherPronoun = otherPronoun;
    }

    public String getOtherProfilePic_url() {
        return otherProfilePic_url;
    }

    public void setOtherProfilePic_url(String otherProfilePic_url) {
        this.otherProfilePic_url = otherProfilePic_url;
    }

    public int getOtherFollowers() {
        return otherFollowers;
    }

    public void setOtherFollowers(int otherFollowers) {
        this.otherFollowers = otherFollowers;
    }
}
