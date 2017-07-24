package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by hungnguyen on 7/18/17.
 */
@Parcel
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
    @SerializedName("is_friend")
    private boolean isFriend;

    public SemiUserInfoResponse() {
    }

    public SemiUserInfoResponse(int user, String displayName, String profileUrl) {
        this.user = user;
        this.displayName = displayName;
        this.profileUrl = profileUrl;
    }

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

    public boolean getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(boolean isFriend) {
        this.isFriend = isFriend;
    }
}
