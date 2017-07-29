package com.hoocons.hoocons_android.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hungnguyen on 7/13/17.
 */

public class Media {
    @SerializedName("url")
    private String url;
    @SerializedName("type")
    private String type;
    @SerializedName("thumbnail")
    String thumbNail;

    public Media(String url, String type) {
        this.url = url;
        this.type = type;
    }

    public Media(String url, String type, String thumbNail) {
        this.url = url;
        this.type = type;
        this.thumbNail = thumbNail;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
