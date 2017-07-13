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

    public Media(String url, String type) {
        this.url = url;
        this.type = type;
    }
}
