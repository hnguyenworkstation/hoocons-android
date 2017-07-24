package com.hoocons.hoocons_android.Parcel;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;
import com.hoocons.hoocons_android.Networking.Responses.LocationResponse;
import com.hoocons.hoocons_android.Networking.Responses.MediaResponse;
import com.hoocons.hoocons_android.Networking.Responses.SemiUserInfoResponse;
import com.hoocons.hoocons_android.Networking.Responses.TagResponse;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import java.util.List;

/**
 * Created by hungnguyen on 7/22/17.
 */
@Parcel
public class EventParcel {
    int eventId;
    SemiUserInfoResponse userInfo;
    String textContent;
    String eventType;
    String privacy;
    boolean isEdited;
    boolean isReported;
    String createAt;
    String lastActionAt;
    List<MediaResponse> medias;

    @ParcelPropertyConverter(MultiTagsParcel.class)
    List<TagResponse> tags;
    LocationResponse checkInLocation;
    int reportsCount;
    int likeCount;
    boolean isLiked;

    public EventParcel() {

    }

    public EventParcel(int id, int likeCount, boolean isLiked) {
        this.eventId = id;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }

    public int getId() {
        return eventId;
    }

    public void setId(int id) {
        this.eventId = id;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
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

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
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

    public LocationResponse getCheckInLocation() {
        return checkInLocation;
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
}
