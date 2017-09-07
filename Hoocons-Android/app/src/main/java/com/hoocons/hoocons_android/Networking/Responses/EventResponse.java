package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Networking.Requests.LocationRequest;

import java.util.List;

/**
 * Created by hungnguyen on 7/15/17.
 */
public class EventResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("author")
    private SemiUserInfoResponse author;
    @SerializedName("title")
    private String title;
    @SerializedName("text_content")
    private String textContent;
    @SerializedName("general_type")
    private String generalType;
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
    private EventResponse containEvent;
    @SerializedName("posted_location")
    private LocationResponse postedLocation;
    @SerializedName("checkin_location")
    private LocationResponse checkinLocation;
    @SerializedName("tagged_location")
    private LocationResponse taggedLocation;
    @SerializedName("friend")
    private MiniUserResponse friend;
    @SerializedName("likes_count")
    private int likesCount;
    @SerializedName("comments_count")
    private int commentsCount;
    @SerializedName("reports_count")
    private int reportsCount;
    @SerializedName("is_liked")
    private boolean isLiked;
    @SerializedName("is_reported")
    private boolean isReported;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGeneralType() {
        return generalType;
    }

    public void setGeneralType(String generalType) {
        this.generalType = generalType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocationResponse getCheckinLocation() {
        return checkinLocation;
    }

    public void setCheckinLocation(LocationResponse checkinLocation) {
        this.checkinLocation = checkinLocation;
    }

    public LocationResponse getTaggedLocation() {
        return taggedLocation;
    }

    public void setTaggedLocation(LocationResponse taggedLocation) {
        this.taggedLocation = taggedLocation;
    }

    public MiniUserResponse getFriend() {
        return friend;
    }

    public void setFriend(MiniUserResponse friend) {
        this.friend = friend;
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