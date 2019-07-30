package com.example.mysqlstuff.objects;

public class Comment {



    private String userID;
    private String reviewID;
    private String userName;
    private String userAvatarURL;
    private String comment;

    public Comment() {
    }

    public Comment(String userID, String reviewID, String userName, String userAvatarURL, String comment) {
        this.userID = userID;
        this.reviewID = reviewID;
        this.userName = userName;
        this.userAvatarURL = userAvatarURL;
        this.comment = comment;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatarURL() {
        return userAvatarURL;
    }

    public void setUserAvatarURL(String userAvatarURL) {
        this.userAvatarURL = userAvatarURL;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
