package com.hoocons.hoocons_android.Parcel;

import com.hoocons.hoocons_android.Models.Topic;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import java.util.List;

/**
 * Created by hungnguyen on 8/23/17.
 */
@Parcel
public class UserParcel {
    int userId;
    String userDisplayName;
    String userNickname;
    String userProfileUrl;
    String userWallpaperUrl;
    @ParcelPropertyConverter(MultiTopicsParcel.class)
    List<Topic> hobbies;
    boolean isFriend;
    boolean isFriendRequested;

    public UserParcel() {

    }

    public boolean isFriendRequested() {
        return isFriendRequested;
    }

    public void setFriendRequested(boolean friendRequested) {
        isFriendRequested = friendRequested;
    }

    public List<Topic> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<Topic> hobbies) {
        this.hobbies = hobbies;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserProfileUrl() {
        return userProfileUrl;
    }

    public void setUserProfileUrl(String userProfileUrl) {
        this.userProfileUrl = userProfileUrl;
    }

    public String getUserWallpaperUrl() {
        return userWallpaperUrl;
    }

    public void setUserWallpaperUrl(String userWallpaperUrl) {
        this.userWallpaperUrl = userWallpaperUrl;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }
}
