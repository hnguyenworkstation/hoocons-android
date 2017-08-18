package com.hoocons.hoocons_android.Parcel;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Models.Topic;
import com.hoocons.hoocons_android.Networking.Responses.MediaResponse;

import org.parceler.Parcel;
import org.parceler.ParcelProperty;
import org.parceler.ParcelPropertyConverter;

import java.util.List;

/**
 * Created by hungnguyen on 8/17/17.
 */
@Parcel
public class ChannelProfileParcel {
    int id;
    String name;
    String subname;
    String about;
    String profileUrl;
    String wallpaperUrl;
    String privacy;

    @ParcelPropertyConverter(MultiTopicsParcel.class)
    List<Topic> topics;

    @ParcelPropertyConverter(MediaListParcel.class)
    List<MediaResponse> promotedMedias;

    int favoriteCount;
    int membersCount;
    int followersCount;
    boolean isMember;
    boolean isFavored;
    boolean isFollower;
    int reportsCount;
    boolean isReported;
    boolean isOwner;

    public ChannelProfileParcel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getWallpaperUrl() {
        return wallpaperUrl;
    }

    public void setWallpaperUrl(String wallpaperUrl) {
        this.wallpaperUrl = wallpaperUrl;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public List<MediaResponse> getPromotedMedias() {
        return promotedMedias;
    }

    public void setPromotedMedias(List<MediaResponse> promotedMedias) {
        this.promotedMedias = promotedMedias;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }

    public boolean isFavored() {
        return isFavored;
    }

    public void setFavored(boolean favored) {
        isFavored = favored;
    }

    public boolean isFollower() {
        return isFollower;
    }

    public void setFollower(boolean follower) {
        isFollower = follower;
    }

    public int getReportsCount() {
        return reportsCount;
    }

    public void setReportsCount(int reportsCount) {
        this.reportsCount = reportsCount;
    }

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }
}
