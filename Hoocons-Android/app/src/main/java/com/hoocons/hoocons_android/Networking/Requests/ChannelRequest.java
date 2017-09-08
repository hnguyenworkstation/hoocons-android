package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hungnguyen on 8/16/17.
 */

public class ChannelRequest {
    @SerializedName("name")
    private String name;
    @SerializedName("subname")
    private String subname;
    @SerializedName("about")
    private String about;
    @SerializedName("profile_url")
    private String profileUrl;
    @SerializedName("topics")
    private List<String> topics;
    @SerializedName("privacy")
    private String privacy;
    @SerializedName("wallpaper_url")
    private String wallpaperUrl;
    @SerializedName("location")
    private LocationRequest locationRequest;

    public ChannelRequest(String name, String subname, String about, String profileUrl,
                          List<String> topics, String privacy, String wallpaperUrl,
                          LocationRequest locationRequest) {
        this.name = name;
        this.subname = subname;
        this.about = about;
        this.profileUrl = profileUrl;
        this.topics = topics;
        this.privacy = privacy;
        this.wallpaperUrl = wallpaperUrl;
        this.locationRequest = locationRequest;
    }

    public ChannelRequest(String name, String subname,
                          String about, String profileUrl,
                          List<String> topics, String privacy, String wallpaperUrl) {
        this.name = name;
        this.subname = subname;
        this.about = about;
        this.profileUrl = profileUrl;
        this.topics = topics;
        this.privacy = privacy;
        this.wallpaperUrl = wallpaperUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getWallpaperUrl() {
        return wallpaperUrl;
    }

    public void setWallpaperUrl(String wallpaperUrl) {
        this.wallpaperUrl = wallpaperUrl;
    }

    public LocationRequest getLocationRequest() {
        return locationRequest;
    }

    public void setLocationRequest(LocationRequest locationRequest) {
        this.locationRequest = locationRequest;
    }
}
