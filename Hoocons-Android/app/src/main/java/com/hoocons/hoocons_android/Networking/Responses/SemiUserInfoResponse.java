package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Models.Topic;
import com.hoocons.hoocons_android.Parcel.MultiTopicsParcel;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import java.util.List;

/**
 * Created by hungnguyen on 7/18/17.
 */
@Parcel
public class SemiUserInfoResponse {
    @SerializedName("user")
    int user;
    @SerializedName("display_name")
    String displayName;
    @SerializedName("nickname")
    String nickname;
    @SerializedName("profile_url")
    String profileUrl;
    @SerializedName("wallpaper_url")
    String wallpaperUrl;
    @SerializedName("last_action_at")
    String lastActionAt;
    @SerializedName("location")
    LocationResponse location;
    @SerializedName("is_sharing_location")
    boolean isSharingLocation;
    @SerializedName("is_friend")
    boolean isFriend;
    @SerializedName("friend_requested")
    boolean isFriendRequested;

    @ParcelPropertyConverter(MultiTopicsParcel.class)
    @SerializedName("hobbies")
    List<Topic> hobbies;

    public SemiUserInfoResponse() {
    }

    public SemiUserInfoResponse(int user, String displayName, String profileUrl) {
        this.user = user;
        this.displayName = displayName;
        this.profileUrl = profileUrl;
    }

    public boolean isFriendRequested() {
        return isFriendRequested;
    }

    public void setFriendRequested(boolean friendRequested) {
        isFriendRequested = friendRequested;
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

    public LocationResponse getLocation() {
        return location;
    }

    public void setLocation(LocationResponse location) {
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

    public boolean isSharingLocation() {
        return isSharingLocation;
    }

    public void setSharingLocation(boolean sharingLocation) {
        isSharingLocation = sharingLocation;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public List<Topic> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<Topic> hobbies) {
        this.hobbies = hobbies;
    }

    public String getWallpaperUrl() {
        return wallpaperUrl;
    }

    public void setWallpaperUrl(String wallpaperUrl) {
        this.wallpaperUrl = wallpaperUrl;
    }
}
