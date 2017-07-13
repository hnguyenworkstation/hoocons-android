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

    public EventInfoRequest(String textContent, ArrayList<Media> medias,
                            ArrayList<String> tags, String privacy, long latitude,
                            long longitude, String eventType) {
        this.textContent = textContent;
        this.medias = medias;
        this.tags = tags;
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

}
