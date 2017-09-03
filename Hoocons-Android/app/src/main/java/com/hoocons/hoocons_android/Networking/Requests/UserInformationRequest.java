package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Models.Media;

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
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("location_name")
    private String locationName;
    @SerializedName("city")
    private String city;
    @SerializedName("province")
    private String province;
    @SerializedName("state")
    private String state;
    @SerializedName("zipcode")
    private int zipcode;
    @SerializedName("country")
    private String country;

    public UserInformationRequest(String nickname) {
        this.nickname = nickname;
    }

    public UserInformationRequest(String displayName, String nickname, String gender, String birthday,
                                  Media profileMedia, double longitude, double latitude) {
        this.displayName = displayName;
        this.nickname = nickname;
        this.gender = gender;
        this.birthday = birthday;
        this.profileMedia = profileMedia;
        this.longitude = longitude;
        this.latitude =latitude;
    }

    public UserInformationRequest(String gender, String displayName, String nickname,
                                  String birthday, String work, Media profileMedia,
                                  Media wallpaperMedia, double longitude, double latitude,
                                  String locationName, String city, String province,
                                  String state, int zipcode, String country) {
        this.gender = gender;
        this.displayName = displayName;
        this.nickname = nickname;
        this.birthday = birthday;
        this.work = work;
        this.profileMedia = profileMedia;
        this.wallpaperMedia = wallpaperMedia;
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationName = locationName;
        this.city = city;
        this.province = province;
        this.state = state;
        this.zipcode = zipcode;
        this.country = country;
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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
