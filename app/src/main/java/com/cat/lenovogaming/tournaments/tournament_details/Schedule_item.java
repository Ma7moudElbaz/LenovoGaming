package com.cat.lenovogaming.tournaments.tournament_details;

public class Schedule_item {
    int id;
    String ImageUrl;

    public Schedule_item(int id, String imageUrl) {
        this.id = id;
        ImageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
