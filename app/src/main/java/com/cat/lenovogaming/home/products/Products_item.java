package com.cat.lenovogaming.home.products;

public class Products_item {
    int id;
    String title,content,imgUrl;
    int isAccessory;

    public Products_item(int id, String title, String content, String imgUrl, int isAccessory) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.isAccessory = isAccessory;
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

    public int getIsAccessory() {
        return isAccessory;
    }

    public void setIsAccessory(int isAccessory) {
        this.isAccessory = isAccessory;
    }
}
