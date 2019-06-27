package com.example.mysqlstuff.model;

public class Game {

    private int gameId;
    private String title;
    private String releaseYear;
    private String developer;
    private String averageRating;
    private String description;
    private String image_url;

    public Game() {
    }

    public Game(int gameId, String title, String releaseYear, String developer, String averageRating, String description, String image_url) {
        this.gameId = gameId;
        this.title = title;
        this.releaseYear = releaseYear;
        this.developer = developer;
        this.averageRating = averageRating;
        this.description = description;
        this.image_url = image_url;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
