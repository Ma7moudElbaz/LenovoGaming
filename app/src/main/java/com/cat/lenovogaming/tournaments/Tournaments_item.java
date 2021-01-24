package com.cat.lenovogaming.tournaments;

public class Tournaments_item {
    int id;
    String title,content,imgUrl;

    public Tournaments_item(int id, String title, String content, String imgUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
