package com.example.mysqlstuff.model;

public class Review {

    private int reviewId;
    private String gameName;
    private String authorName;
    private String authorPictureUrl;
    private String rating;
    private String review;
    private String gamePictureUrl;
    private String likes;
    private int authorId;
    private int gameId;

    public Review() {
    }

    public Review(int reviewId, String gameName, String authorName, String authorPictureUrl, String rating, String review, String gamePictureUrl, String likes, int authorId, int gameId) {
        this.reviewId = reviewId;
        this.gameName = gameName;
        this.authorName = authorName;
        this.authorPictureUrl = authorPictureUrl;
        this.rating = rating;
        this.review = review;
        this.gamePictureUrl = gamePictureUrl;
        this.likes = likes;
        this.authorId = authorId;
        this.gameId = gameId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorPictureUrl() {
        return authorPictureUrl;
    }

    public void setAuthorPictureUrl(String authorPictureUrl) {
        this.authorPictureUrl = authorPictureUrl;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getGamePictureUrl() {
        return gamePictureUrl;
    }

    public void setGamePictureUrl(String gamePictureUrl) {
        this.gamePictureUrl = gamePictureUrl;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
