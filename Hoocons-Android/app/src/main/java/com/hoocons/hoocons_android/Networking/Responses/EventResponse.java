package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hungnguyen on 7/15/17.
 */

public class EventResponse {
    @SerializedName("event_id")
    private int eventId;
    @SerializedName("user_info")
    private UserInfo userInfo;
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
    @SerializedName("location")
    private LocationResponse location;
    @SerializedName("check_in_location")
    private LocationResponse checkInLocation;
    @SerializedName("likes_count")
    private int likesCount;
    @SerializedName("comments_count")
    private int commentsCount;
    @SerializedName("is_liked")
    private boolean isLiked;

    public EventResponse() {

    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
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

    public LocationResponse getLocation() {
        return location;
    }

    public void setLocation(LocationResponse location) {
        this.location = location;
    }

    public LocationResponse getCheckInLocation() {
        return checkInLocation;
    }

    public void setCheckInLocation(LocationResponse checkInLocation) {
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

    public static class UserInfo {
        @SerializedName("user")
        private int user;
        @SerializedName("display_name")
        private String displayName;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("profile_url")
        private String profileUrl;
        @SerializedName("last_action_at")
        private String lastActionAt;
        @SerializedName("location")
        private String location;
        @SerializedName("is_sharing_location")
        private boolean isSharingLocation;

        public int getUser() {
            return user;
        }

        public void setUser(int user) {
            this.user = user;
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

        public String getProfileUrl() {
            return profileUrl;
        }

        public void setProfileUrl(String profileUrl) {
            this.profileUrl = profileUrl;
        }

        public String getLastActionAt() {
            return lastActionAt;
        }

        public void setLastActionAt(String lastActionAt) {
            this.lastActionAt = lastActionAt;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public boolean getIsSharingLocation() {
            return isSharingLocation;
        }

        public void setIsSharingLocation(boolean isSharingLocation) {
            this.isSharingLocation = isSharingLocation;
        }
    }
}
