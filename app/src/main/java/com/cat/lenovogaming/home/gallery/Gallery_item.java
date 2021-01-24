package com.cat.lenovogaming.home.gallery;

public class Gallery_item {
    int id;
    String imgUrl, externalUrl;

    public Gallery_item(int id, String imgUrl, String externalUrl) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.externalUrl = externalUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }
}