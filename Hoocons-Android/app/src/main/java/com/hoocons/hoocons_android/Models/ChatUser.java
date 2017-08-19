package com.hoocons.hoocons_android.Models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hungnguyen on 8/6/17.
 */
@IgnoreExtraProperties
public class ChatUser implements Serializable {
    @SerializedName("user_id")
    private int userId;
    @SerializedName("user_display_name")
    private String displayName;
    @SerializedName("is_typing")
    private boolean isTyping;

    public ChatUser(int userId) {
        this.userId = userId;
    }

    public ChatUser(int userId, String displayName, boolean isTyping) {
        this.userId = userId;
        this.displayName = displayName;
        this.isTyping = isTyping;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }
}
