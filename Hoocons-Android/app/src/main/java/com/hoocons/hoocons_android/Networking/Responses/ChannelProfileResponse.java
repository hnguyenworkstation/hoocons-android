package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Models.Topic;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by hungnguyen on 8/17/17.
 */
public class ChannelProfileResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("subname")
    private String subname;
    @SerializedName("about")
    private String about;
    @SerializedName("profile_url")
    private String profileUrl;
    @SerializedName("wallpaper_url")
    private String wallpaperUrl;
    @SerializedName("privacy")
    private String privacy;
    @SerializedName("topics")
    private List<Topic> topics;
    @SerializedName("promoted_medias")
    private List<MediaResponse> promotedMedias;
    @SerializedName("location")
    private LocationResponse location;
    @SerializedName("top_users")
    private List<SemiUserInfoResponse> topUsers;
    @SerializedName("friend_members_count")
    private int friendMembersCount;
    @SerializedName("favorite_count")
    private int favoriteCount;
    @SerializedName("members_count")
    private int membersCount;
    @SerializedName("followers_count")
    private int followersCount;
    @SerializedName("is_member")
    private boolean isMember;
    @SerializedName("is_favored")
    private boolean isFavored;
    @SerializedName("is_follower")
    private boolean isFollower;
    @SerializedName("is_owner")
    private boolean isOwner;
    @SerializedName("reports_count")
    private int reportsCount;
    @SerializedName("is_reported")
    private boolean isReported;



    public ChannelProfileResponse(int id, String name, String subname, String about,
                                  String profileUrl, String wallpaperUrl, String privacy,
                                  List<Topic> topics, List<MediaResponse> promotedMedias,
                                  LocationResponse location, List<SemiUserInfoResponse> topUsers,
                                  int friendMembersCount, int favoriteCount, int membersCount,
                                  int followersCount, boolean isMember, boolean isFavored,
                                  boolean isFollower, boolean isOwner, int reportsCount,
                                  boolean isReported) {
        this.id = id;
        this.name = name;
        this.subname = subname;
        this.about = about;
        this.profileUrl = profileUrl;
        this.wallpaperUrl = wallpaperUrl;
        this.privacy = privacy;
        this.topics = topics;
        this.promotedMedias = promotedMedias;
        this.location = location;
        this.topUsers = topUsers;
        this.friendMembersCount = friendMembersCount;
        this.favoriteCount = favoriteCount;
        this.membersCount = membersCount;
        this.followersCount = followersCount;
        this.isMember = isMember;
        this.isFavored = isFavored;
        this.isFollower = isFollower;
        this.isOwner = isOwner;
        this.reportsCount = reportsCount;
        this.isReported = isReported;
    }

    public ChannelProfileResponse(String name, String subname, String about, String profileUrl,
                                  String wallpaperUrl, String privacy, List<Topic> topics,
                                  List<MediaResponse> promotedMedias, LocationResponse location,
                                  List<SemiUserInfoResponse> topUsers, int friendMembersCount,
                                  int favoriteCount, int membersCount, int followersCount,
                                  boolean isMember, boolean isFavored, boolean isFollower,
                                  boolean isOwner, int reportsCount, boolean isReported) {
        this.name = name;
        this.subname = subname;
        this.about = about;
        this.profileUrl = profileUrl;
        this.wallpaperUrl = wallpaperUrl;
        this.privacy = privacy;
        this.topics = topics;
        this.promotedMedias = promotedMedias;
        this.location = location;
        this.topUsers = topUsers;
        this.friendMembersCount = friendMembersCount;
        this.favoriteCount = favoriteCount;
        this.membersCount = membersCount;
        this.followersCount = followersCount;
        this.isMember = isMember;
        this.isFavored = isFavored;
        this.isFollower = isFollower;
        this.isOwner = isOwner;
        this.reportsCount = reportsCount;
        this.isReported = isReported;
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

    public LocationResponse getLocation() {
        return location;
    }

    public void setLocation(LocationResponse location) {
        this.location = location;
    }

    public List<SemiUserInfoResponse> getTopUsers() {
        return topUsers;
    }

    public void setTopUsers(List<SemiUserInfoResponse> topUsers) {
        this.topUsers = topUsers;
    }

    public int getFriendMembersCount() {
        return friendMembersCount;
    }

    public void setFriendMembersCount(int friendMembersCount) {
        this.friendMembersCount = friendMembersCount;
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

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
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
}
