package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Models.Media;
import com.hoocons.hoocons_android.Models.Topic;

import java.util.List;

/**
 * Created by hungnguyen on 6/17/17.
 */

public class UserInformationRequest {
    @SerializedName("gender")
    private String gender;
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("work")
    private String work;
    @SerializedName("profile_media")
    private Media profileMedia;
    @SerializedName("wallpaper_media")
    private Media wallpaperMedia;
    @SerializedName("location")
    private LocationRequest locationRequest;
    @SerializedName("hobbies")
    private List<Topic> hobbies;

    public UserInformationRequest(String gender, String displayName,
                                  String nickname, String birthday, String work,
                                  Media profileMedia, Media wallpaperMedia,
                                  LocationRequest locationRequest, List<Topic> hobbies) {
        this.gender = gender;
        this.displayName = displayName;
        this.nickname = nickname;
        this.birthday = birthday;
        this.work = work;
        this.profileMedia = profileMedia;
        this.wallpaperMedia = wallpaperMedia;
        this.locationRequest = locationRequest;
        this.hobbies = hobbies;
    }

    public UserInformationRequest(String nickname) {
        this.nickname = nickname;
    }

    public List<Topic> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<Topic> hobbies) {
        this.hobbies = hobbies;
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

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public Media getProfileMedia() {
        return profileMedia;
    }

    public void setProfileMedia(Media profileMedia) {
        this.profileMedia = profileMedia;
    }

    public Media getWallpaperMedia() {
        return wallpaperMedia;
    }

    public void setWallpaperMedia(Media wallpaperMedia) {
        this.wallpaperMedia = wallpaperMedia;
    }

    public LocationRequest getLocationRequest() {
        return locationRequest;
    }

    public void setLocationRequest(LocationRequest locationRequest) {
        this.locationRequest = locationRequest;
    }
}
