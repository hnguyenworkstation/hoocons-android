package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Networking.Requests.LocationRequest;

import java.util.List;

/**
 * Created by hungnguyen on 7/15/17.
 */
public class EventResponse {
    @SerializedName("id")
    int eventId;
    @SerializedName("author")
    SemiUserInfoResponse author;
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
    @SerializedName("posted_location")
    LocationResponse postedLocation;
    @SerializedName("tagged_location")
    LocationResponse taggedLocation;
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

    public EventResponse() {

    }

    public EventResponse(int eventId, SemiUserInfoResponse author, String textContent,
                         String eventType, String privacy, boolean isEdited, String createAt,
                         String lastActionAt, List<MediaResponse> medias, List<TagResponse> tags,
                         EventResponse containEvent, LocationResponse postedLocation,
                         LocationResponse taggedLocation, LocationResponse checkInLocation,
                         int likesCount, int commentsCount, int reportsCount,
                         boolean isLiked, boolean isReported) {
        this.eventId = eventId;
        this.author = author;
        this.textContent = textContent;
        this.eventType = eventType;
        this.privacy = privacy;
        this.isEdited = isEdited;
        this.createAt = createAt;
        this.lastActionAt = lastActionAt;
        this.medias = medias;
        this.tags = tags;
        this.containEvent = containEvent;
        this.postedLocation = postedLocation;
        this.taggedLocation = taggedLocation;
        this.checkInLocation = checkInLocation;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.reportsCount = reportsCount;
        this.isLiked = isLiked;
        this.isReported = isReported;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public SemiUserInfoResponse getAuthor() {
        return author;
    }

    public void setAuthor(SemiUserInfoResponse author) {
        this.author = author;
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

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
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

    public LocationResponse getPostedLocation() {
        return postedLocation;
    }

    public void setPostedLocation(LocationResponse postedLocation) {
        this.postedLocation = postedLocation;
    }

    public LocationResponse getTaggedLocation() {
        return taggedLocation;
    }

    public void setTaggedLocation(LocationResponse taggedLocation) {
        this.taggedLocation = taggedLocation;
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

    public int getReportsCount() {
        return reportsCount;
    }

    public void setReportsCount(int reportsCount) {
        this.reportsCount = reportsCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }
}
