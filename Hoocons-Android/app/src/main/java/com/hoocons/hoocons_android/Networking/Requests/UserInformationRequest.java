package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.EventBus.UserInfoRequest;
import com.hoocons.hoocons_android.Models.Media;

/**
 * Created by hungnguyen on 6/17/17.
 */

public class UserInformationRequest {
    @SerializedName("profile_media")
    private Media profileUrl;
    @SerializedName("gender")
    private String gender;
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("latitude")
    private double latitude;

    public UserInformationRequest(String nickname) {
        this.nickname = nickname;
    }

    public UserInformationRequest(String displayName, String nickname, String gender, String birthday,
                                  Media profileUrl, double longitude, double latitude) {
        this.nickname = nickname;
        this.displayName = displayName;
        this.gender = gender;
        this.birthday = birthday;
        this.profileUrl = profileUrl;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Media getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(Media profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
