package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by hungnguyen on 7/15/17.
 */
@Parcel
public class MediaResponse {
    @SerializedName("id")
    int id;
    @SerializedName("url")
    String url;
    @SerializedName("type")
    String type;
    @SerializedName("thumbnail")
    String thumbNail;

    public MediaResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }
}
