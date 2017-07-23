package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by hungnguyen on 7/15/17.
 */
@Parcel
public class MediaResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("url")
    private String url;
    @SerializedName("type")
    private String type;
    @SerializedName("thumbnail")
    private String thumbNail;

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
