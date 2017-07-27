package com.hoocons.hoocons_android.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hungnguyen on 7/27/17.
 */

public class Sticker {
    @SerializedName("sticker_key")
    private String key;
    @SerializedName("sticker_value")
    private String value;

    public Sticker(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
