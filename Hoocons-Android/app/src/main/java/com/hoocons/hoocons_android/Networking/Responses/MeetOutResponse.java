package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Models.Topic;

import java.util.List;

/**
 * Created by hungnguyen on 7/29/17.
 */

public class MeetOutResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("created_by")
    private UserInfoResponse createdBy;
    @SerializedName("create_at")
    private String createAt;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("promoted_medias")
    private List<MediaResponse> promotedMedias;
    @SerializedName("from_date_time")
    private String fromDateTime;
    @SerializedName("to_date_time")
    private String toDateTime;
    @SerializedName("city")
    private String city;
    @SerializedName("country")
    private String country;
    @SerializedName("privacy")
    private String privacy;
    @SerializedName("is_promoted")
    private boolean isPromoted;
    @SerializedName("posted_at_location")
    private CoordinateResponse postedAtLocation;
    @SerializedName("meetup_location")
    private CoordinateResponse meetupLocation;
    @SerializedName("meetup_location_name")
    private String meetupLocationName;
    @SerializedName("meetup_location_address")
    private String meetupLocationAddress;
    @SerializedName("tags")
    private List<TagResponse> tags;
    @SerializedName("topics")
    private List<Topic> topics;
    @SerializedName("like_count")
    private int likeCount;
    @SerializedName("report_count")
    private int reportCount;
    @SerializedName("is_reported")
    private boolean isReported;

    public MeetOutResponse(int id, UserInfoResponse createdBy,
                           String createAt, String name, String description,
                           List<MediaResponse> promotedMedias, String fromDateTime,
                           String toDateTime, String city, String country, String privacy,
                           boolean isPromoted, CoordinateResponse postedAtLocation,
                           CoordinateResponse meetupLocation, String meetupLocationName,
                           String meetupLocationAddress, List<TagResponse> tags, List<Topic> topics,
                           int likeCount, int reportCount, boolean isReported) {
        this.id = id;
        this.createdBy = createdBy;
        this.createAt = createAt;
        this.name = name;
        this.description = description;
        this.promotedMedias = promotedMedias;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.city = city;
        this.country = country;
        this.privacy = privacy;
        this.isPromoted = isPromoted;
        this.postedAtLocation = postedAtLocation;
        this.meetupLocation = meetupLocation;
        this.meetupLocationName = meetupLocationName;
        this.meetupLocationAddress = meetupLocationAddress;
        this.tags = tags;
        this.topics = topics;
        this.likeCount = likeCount;
        this.reportCount = reportCount;
        this.isReported = isReported;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserInfoResponse getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserInfoResponse createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MediaResponse> getPromotedMedias() {
        return promotedMedias;
    }

    public void setPromotedMedias(List<MediaResponse> promotedMedias) {
        this.promotedMedias = promotedMedias;
    }

    public String getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(String fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public String getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(String toDateTime) {
        this.toDateTime = toDateTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public boolean isPromoted() {
        return isPromoted;
    }

    public void setPromoted(boolean promoted) {
        isPromoted = promoted;
    }

    public CoordinateResponse getPostedAtLocation() {
        return postedAtLocation;
    }

    public void setPostedAtLocation(CoordinateResponse postedAtLocation) {
        this.postedAtLocation = postedAtLocation;
    }

    public CoordinateResponse getMeetupLocation() {
        return meetupLocation;
    }

    public void setMeetupLocation(CoordinateResponse meetupLocation) {
        this.meetupLocation = meetupLocation;
    }

    public String getMeetupLocationName() {
        return meetupLocationName;
    }

    public void setMeetupLocationName(String meetupLocationName) {
        this.meetupLocationName = meetupLocationName;
    }

    public String getMeetupLocationAddress() {
        return meetupLocationAddress;
    }

    public void setMeetupLocationAddress(String meetupLocationAddress) {
        this.meetupLocationAddress = meetupLocationAddress;
    }

    public List<TagResponse> getTags() {
        return tags;
    }

    public void setTags(List<TagResponse> tags) {
        this.tags = tags;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }
}
