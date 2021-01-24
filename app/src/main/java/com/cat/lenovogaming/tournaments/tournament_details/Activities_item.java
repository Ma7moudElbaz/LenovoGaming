package com.cat.lenovogaming.tournaments.tournament_details;

public class Activities_item {
    int id;
    String title,content,imageUrl;
    int hasFields;

    public Activities_item(int id, String title, String content, String imageUrl, int hasFields) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.hasFields = hasFields;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getHasFields() {
        return hasFields;
    }

    public void setHasFields(int hasFields) {
        this.hasFields = hasFields;
    }
}
