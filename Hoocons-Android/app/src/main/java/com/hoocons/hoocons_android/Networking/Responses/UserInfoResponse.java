package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Models.SimpleMeetout;
import com.hoocons.hoocons_android.Models.Topic;

import java.util.List;

/**
 * Created by hungnguyen on 6/16/17.
 */
public class UserInfoResponse {
    @SerializedName("user")
    private int userPK;
    @SerializedName("username")
    private String username;
    @SerializedName("location")
    private LocationResponse location;
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("gender")
    private String gender;
    @SerializedName("profile_url")
    private String profileUrl;
    @SerializedName("wallpaper_url")
    private String wallPaperUrl;
    @SerializedName("join_date")
    private String joinDate;
    @SerializedName("last_action_at")
    private String lastActionAt;
    @SerializedName("is_sharing_location")
    private boolean isSharingLocation;
    @SerializedName("meetout_created")
    private List<SimpleMeetout> meetoutCreatedList;
    @SerializedName("is_self")
    private boolean isSelf;
    @SerializedName("is_friend")
    private boolean isFriend;
    @SerializedName("hobbies")
    private List<Topic> hobbies;
    @SerializedName("friend_requested")
    private boolean isFriendRequested;

    public UserInfoResponse() {
    }

    public UserInfoResponse(String displayName, String nickname, String profileUrl) {
        this.displayName = displayName;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
    }

    public boolean isFriendRequested() {
        return isFriendRequested;
    }

    public void setFriendRequested(boolean friendRequested) {
        isFriendRequested = friendRequested;
    }

    public int getUserPK() {
        return userPK;
    }

    public void setUserPK(int userPk) {
        this.userPK = userPk;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocationResponse getLocation() {
        return location;
    }

    public void setLocation(LocationResponse location) {
        this.location = location;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getLastActionAt() {
        return lastActionAt;
    }

    public void setLastActionAt(String lastActionAt) {
        this.lastActionAt = lastActionAt;
    }

    public boolean getIsSharingLocation() {
        return isSharingLocation;
    }

    public void setIsSharingLocation(boolean isSharingLocation) {
        this.isSharingLocation = isSharingLocation;
    }

    public String getWallPaperUrl() {
        return wallPaperUrl;
    }

    public void setWallPaperUrl(String wallPaperUrl) {
        this.wallPaperUrl = wallPaperUrl;
    }

    public boolean isSharingLocation() {
        return isSharingLocation;
    }

    public void setSharingLocation(boolean sharingLocation) {
        isSharingLocation = sharingLocation;
    }

    public List<SimpleMeetout> getMeetoutCreatedList() {
        return meetoutCreatedList;
    }

    public void setMeetoutCreatedList(List<SimpleMeetout> meetoutCreatedList) {
        this.meetoutCreatedList = meetoutCreatedList;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
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
}
