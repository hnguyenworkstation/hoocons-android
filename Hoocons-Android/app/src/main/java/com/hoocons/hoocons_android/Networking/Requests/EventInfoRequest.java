package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Models.Media;

import java.util.ArrayList;

/**
 * Created by hungnguyen on 7/12/17.
 */

public class EventInfoRequest {
    @SerializedName("text_content")
    private String textContent;
    @SerializedName("medias")
    private ArrayList<Media> medias;
    @SerializedName("tags")
    private ArrayList<String> tags;
    @SerializedName("privacy")
    private String privacy;
    @SerializedName("latitude")
    private long latitude;
    @SerializedName("longitude")
    private long longitude;
    @SerializedName("srid")
    private final int srid = 4326;
    @SerializedName("event_type")
    private String eventType;
    @SerializedName("meetup_id")
    private int meetUpId;
    @SerializedName("channel_id")
    private int channelId;
    @SerializedName("on_profile")
    private boolean isOnProfile;

    public EventInfoRequest(String textContent, ArrayList<Media> medias,
                            ArrayList<String> tags, String privacy, long latitude,
                            long longitude, String eventType, boolean isOnProfile) {
        this.textContent = textContent;
        this.isOnProfile = isOnProfile;

        if (medias != null) {
            this.medias = medias;
        } else {
            this.medias = new ArrayList<>();
        }

        if (tags != null) {
            this.tags = tags;
        } else {
            this.tags = new ArrayList<>();
        }

        this.privacy = privacy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.eventType = eventType;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public ArrayList<Media> getMedias() {
        return medias;
    }

    public void setMedias(ArrayList<Media> medias) {
        this.medias = medias;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public int getSrid() {
        return srid;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getMeetUpId() {
        return meetUpId;
    }

    public void setMeetUpId(int meetUpId) {
        this.meetUpId = meetUpId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }
}
