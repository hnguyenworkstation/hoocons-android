package com.hoocons.hoocons_android.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hungnguyen on 8/6/17.
 */

public class ChatUser implements Serializable {
    @SerializedName("user_id")
    private int userId;

    public ChatUser(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
