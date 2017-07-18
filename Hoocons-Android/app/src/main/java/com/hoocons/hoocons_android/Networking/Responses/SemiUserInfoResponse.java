package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hungnguyen on 7/18/17.
 */

public class SemiUserInfoResponse {
    @SerializedName("user")
    private int user;
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("profile_url")
    private String profileUrl;
    @SerializedName("last_action_at")
    private String lastActionAt;
    @SerializedName("location")
    private String location;
    @SerializedName("is_sharing_location")
    private boolean isSharingLocation;

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getLastActionAt() {
        return lastActionAt;
    }

    public void setLastActionAt(String lastActionAt) {
        this.lastActionAt = lastActionAt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean getIsSharingLocation() {
        return isSharingLocation;
    }

    public void setIsSharingLocation(boolean isSharingLocation) {
        this.isSharingLocation = isSharingLocation;
    }
}
