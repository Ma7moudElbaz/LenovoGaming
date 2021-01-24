package com.cat.lenovogaming.tournaments.tournament_details;

public class Games_item {
    int gameId ,tournamentId;
    String title,date,gameName,prize,imageUrl,thumbURL;

    public Games_item(int gameId, int tournamentId, String title, String date, String gameName, String prize, String imageUrl, String thumbURL) {
        this.gameId = gameId;
        this.tournamentId = tournamentId;
        this.title = title;
        this.date = date;
        this.gameName = gameName;
        this.prize = prize;
        this.imageUrl = imageUrl;
        this.thumbURL = thumbURL;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbURL() {
        return thumbURL;
    }

    public void setThumbURL(String thumbURL) {
        this.thumbURL = thumbURL;
    }
}
