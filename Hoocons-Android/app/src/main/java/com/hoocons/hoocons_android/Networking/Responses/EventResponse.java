package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hungnguyen on 7/15/17.
 */

public class EventResponse {
    @SerializedName("event_id")
    private int eventId;
    @SerializedName("on_profile")
    private boolean onProfile;
    @SerializedName("user_info")
    private SemiUserInfoResponse userInfo;
    @SerializedName("text_content")
    private String textContent;
    @SerializedName("event_type")
    private String eventType;
    @SerializedName("privacy")
    private String privacy;
    @SerializedName("is_edited")
    private boolean isEdited;
    @SerializedName("create_at")
    private String createAt;
    @SerializedName("last_action_at")
    private String lastActionAt;
    @SerializedName("medias")
    private List<MediaResponse> medias;
    @SerializedName("tags")
    private List<TagResponse> tags;
    @SerializedName("contain_event")
    private String containEvent;
    @SerializedName("location")
    private LocationResponse location;
    @SerializedName("check_in_location")
    private String checkInLocation;
    @SerializedName("likes_count")
    private int likesCount;
    @SerializedName("comments_count")
    private int commentsCount;
    @SerializedName("is_liked")
    private boolean isLiked;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public boolean getOnProfile() {
        return onProfile;
    }

    public void setOnProfile(boolean onProfile) {
        this.onProfile = onProfile;
    }

    public SemiUserInfoResponse getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(SemiUserInfoResponse userInfo) {
        this.userInfo = userInfo;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public boolean getIsEdited() {
        return isEdited;
    }

    public void setIsEdited(boolean isEdited) {
        this.isEdited = isEdited;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getLastActionAt() {
        return lastActionAt;
    }

    public void setLastActionAt(String lastActionAt) {
        this.lastActionAt = lastActionAt;
    }

    public List<MediaResponse> getMedias() {
        return medias;
    }

    public void setMedias(List<MediaResponse> medias) {
        this.medias = medias;
    }

    public List<TagResponse> getTags() {
        return tags;
    }

    public void setTags(List<TagResponse> tags) {
        this.tags = tags;
    }

    public String getContainEvent() {
        return containEvent;
    }

    public void setContainEvent(String containEvent) {
        this.containEvent = containEvent;
    }

    public LocationResponse getLocation() {
        return location;
    }

    public void setLocation(LocationResponse location) {
        this.location = location;
    }

    public String getCheckInLocation() {
        return checkInLocation;
    }

    public void setCheckInLocation(String checkInLocation) {
        this.checkInLocation = checkInLocation;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }
}
