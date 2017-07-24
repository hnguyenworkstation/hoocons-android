package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Parcel.MediaListParcel;
import com.hoocons.hoocons_android.Parcel.MultiTagsParcel;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import java.util.List;

/**
 * Created by hungnguyen on 7/15/17.
 */
public class EventResponse {
    @SerializedName("event_id")
    int eventId;
    @SerializedName("on_profile")
    boolean onProfile;
    @SerializedName("user_info")
    SemiUserInfoResponse userInfo;
    @SerializedName("text_content")
    String textContent;
    @SerializedName("event_type")
    String eventType;
    @SerializedName("privacy")
    String privacy;
    @SerializedName("is_edited")
    boolean isEdited;
    @SerializedName("create_at")
    String createAt;
    @SerializedName("last_action_at")
    String lastActionAt;
    @SerializedName("medias")
    List<MediaResponse> medias;
    @SerializedName("tags")
    List<TagResponse> tags;
    @SerializedName("contain_event")
    EventResponse containEvent;
    @SerializedName("location")
    LocationResponse location;
    @SerializedName("checkin_location")
    LocationResponse checkInLocation;
    @SerializedName("likes_count")
    int likesCount;
    @SerializedName("comments_count")
    int commentsCount;
    @SerializedName("reports_count")
    int reportsCount;
    @SerializedName("is_liked")
    boolean isLiked;
    @SerializedName("is_reported")
    boolean isReported;
    @SerializedName("checkin_name")
    String checkinName;
    @SerializedName("checkin_address")
    String checkinAddress;
    @SerializedName("checkin_place_id")
    String checkinPlaceId;

    public EventResponse() {

    }

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

    public EventResponse getContainEvent() {
        return containEvent;
    }

    public void setContainEvent(EventResponse containEvent) {
        this.containEvent = containEvent;
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

    public void LocationResponse(LocationResponse checkInLocation) {
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

    public boolean isOnProfile() {
        return onProfile;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getCheckinName() {
        return checkinName;
    }

    public void setCheckinName(String checkinName) {
        this.checkinName = checkinName;
    }

    public String getCheckinAddress() {
        return checkinAddress;
    }

    public void setCheckinAddress(String checkinAddress) {
        this.checkinAddress = checkinAddress;
    }

    public String getCheckinPlaceId() {
        return checkinPlaceId;
    }

    public void setCheckinPlaceId(String checkinPlaceId) {
        this.checkinPlaceId = checkinPlaceId;
    }

    public void setCheckInLocation(LocationResponse checkInLocation) {
        this.checkInLocation = checkInLocation;
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
