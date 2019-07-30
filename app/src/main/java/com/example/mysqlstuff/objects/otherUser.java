package com.example.mysqlstuff.objects;

public class otherUser {

    private int otherUserId;
    private String otherUsername;
    private String otherBio;
    private String otherProfilePic_url;
    private int otherFollowers;

    public otherUser() {
    }

    public otherUser(int otherUserID, String otherUsername, String otherBio, String otherProfilePic_url, int otherFollowers) {
        this.otherUserId = otherUserID;
        this.otherUsername = otherUsername;
        this.otherBio = otherBio;
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

    public String getOtherBio() {
        return otherBio;
    }

    public void setOtherBio(String otherBio) {
        this.otherBio = otherBio;
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
