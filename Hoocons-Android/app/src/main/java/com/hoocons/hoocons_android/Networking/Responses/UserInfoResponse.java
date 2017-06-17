package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hungnguyen on 6/16/17.
 */
public class UserInfoResponse {
    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("gender")
    private String gender;
    @SerializedName("profile_url")
    private String profileUrl;
    @SerializedName("location")
    private float[] location;
    @SerializedName("last_online")
    private String lastOnline;
    @SerializedName("friends")
    private List<String> friends;
    @SerializedName("friends_request_from")
    private List<String> friendsRequestFrom;
    @SerializedName("friends_request_to")
    private List<String> friendsRequestTo;
    @SerializedName("ignoring_users")
    private List<String> ignoringUsers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public float[] getLocation() {
        return location;
    }

    public void setLocation(float[] location) {
        this.location = location;
    }

    public String getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(String lastOnline) {
        this.lastOnline = lastOnline;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getFriendsRequestFrom() {
        return friendsRequestFrom;
    }

    public void setFriendsRequestFrom(List<String> friendsRequestFrom) {
        this.friendsRequestFrom = friendsRequestFrom;
    }

    public List<String> getFriendsRequestTo() {
        return friendsRequestTo;
    }

    public void setFriendsRequestTo(List<String> friendsRequestTo) {
        this.friendsRequestTo = friendsRequestTo;
    }

    public List<String> getIgnoringUsers() {
        return ignoringUsers;
    }

    public void setIgnoringUsers(List<String> ignoringUsers) {
        this.ignoringUsers = ignoringUsers;
    }
}
