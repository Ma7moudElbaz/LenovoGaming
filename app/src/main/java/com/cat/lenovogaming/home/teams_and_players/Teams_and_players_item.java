package com.cat.lenovogaming.home.teams_and_players;

public class Teams_and_players_item {
    int id;
    String title,imgUrl,twichUrl,youtubeUrl,twitterUrl,facebookUrl,instaUrl;

    public Teams_and_players_item(int id, String title, String imgUrl, String twichUrl, String youtubeUrl, String twitterUrl, String facebookUrl, String instaUrl) {
        this.id = id;
        this.title = title;
        this.imgUrl = imgUrl;
        this.twichUrl = twichUrl;
        this.youtubeUrl = youtubeUrl;
        this.twitterUrl = twitterUrl;
        this.facebookUrl = facebookUrl;
        this.instaUrl = instaUrl;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTwichUrl() {
        return twichUrl;
    }

    public void setTwichUrl(String twichUrl) {
        this.twichUrl = twichUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getInstaUrl() {
        return instaUrl;
    }

    public void setInstaUrl(String instaUrl) {
        this.instaUrl = instaUrl;
    }
}
