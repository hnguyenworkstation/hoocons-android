package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Models.Media;
import com.hoocons.hoocons_android.Models.Topic;

import java.util.List;

/**
 * Created by hungnguyen on 7/28/17.
 */
public class MeetOutRequest {
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("from_date_time")
    private String fromDateTime;
    @SerializedName("to_date_time")
    private String toDateTime;
    @SerializedName("meetup_topics")
    private List<Topic> meetupTopics;
    @SerializedName("longitude")
    private int longitude;
    @SerializedName("latitude")
    private int latitude;
    @SerializedName("meetup_longitude")
    private int meetupLongitude;
    @SerializedName("meetup_latitude")
    private int meetupLatitude;
    @SerializedName("meetup_location_name")
    private String meetupLocationName;
    @SerializedName("meetup_location_address")
    private String meetupLocationAddress;
    @SerializedName("medias")
    private List<Media> medias;

    public MeetOutRequest(String name, String description,
                          String fromDateTime, String toDateTime,
                          List<Topic> meetupTopics,
                          int longitude, int latitude,
                          int meetupLongitude, int meetupLatitude,
                          String meetupLocationName, String meetupLocationAddress,
                          List<Media> medias) {
        this.name = name;
        this.description = description;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.meetupTopics = meetupTopics;
        this.longitude = longitude;
        this.latitude = latitude;
        this.meetupLongitude = meetupLongitude;
        this.meetupLatitude = meetupLatitude;
        this.meetupLocationName = meetupLocationName;
        this.meetupLocationAddress = meetupLocationAddress;
        this.medias = medias;
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

    public List<Topic> getMeetupTopics() {
        return meetupTopics;
    }

    public void setMeetupTopics(List<Topic> meetupTopics) {
        this.meetupTopics = meetupTopics;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getMeetupLongitude() {
        return meetupLongitude;
    }

    public void setMeetupLongitude(int meetupLongitude) {
        this.meetupLongitude = meetupLongitude;
    }

    public int getMeetupLatitude() {
        return meetupLatitude;
    }

    public void setMeetupLatitude(int meetupLatitude) {
        this.meetupLatitude = meetupLatitude;
    }

    public String getMeetupLocationAddress() {
        return meetupLocationAddress;
    }

    public void setMeetupLocationAddress(String meetupLocationAddress) {
        this.meetupLocationAddress = meetupLocationAddress;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public String getMeetupLocationName() {
        return meetupLocationName;
    }

    public void setMeetupLocationName(String meetupLocationName) {
        this.meetupLocationName = meetupLocationName;
    }
}
