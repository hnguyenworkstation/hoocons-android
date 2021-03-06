package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Models.Media;
import com.hoocons.hoocons_android.Models.Topic;

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
    private ArrayList<Topic> tags;
    @SerializedName("privacy")
    private String privacy;
    @SerializedName("title")
    private String title;
    @SerializedName("general_type")
    private String generalType;
    @SerializedName("event_type")
    private String eventType;
    @SerializedName("channel_mask")
    private int channelMaskId;
    @SerializedName("target_meetup")
    private int targetMeetup;
    @SerializedName("target_channel")
    private int targetChannel;
    @SerializedName("target_user")
    private int targetUser;
    @SerializedName("posted_location")
    private LocationRequest postedLocation;
    @SerializedName("tagged_location")
    private LocationRequest taggedLocation;
    @SerializedName("checkin_location")
    private LocationRequest checkinLocation;

    public EventInfoRequest() {
    }

    public EventInfoRequest(String textContent, ArrayList<Media> medias, ArrayList<Topic> tags, String privacy,
                            String title, String generalType, String eventType,
                            int channelMaskId, int targetMeetup, int targetChannel,
                            int targetUser, LocationRequest postedLocation,
                            LocationRequest taggedLocation, LocationRequest checkinLocation) {
        this.textContent = textContent;
        this.medias = medias;
        this.tags = tags;
        this.privacy = privacy;
        this.title = title;
        this.generalType = generalType;
        this.eventType = eventType;
        this.channelMaskId = channelMaskId;
        this.targetMeetup = targetMeetup;
        this.targetChannel = targetChannel;
        this.targetUser = targetUser;
        this.postedLocation = postedLocation;
        this.taggedLocation = taggedLocation;
        this.checkinLocation = checkinLocation;
    }

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

    public ArrayList<Topic> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Topic> tags) {
        this.tags = tags;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getChannelMaskId() {
        return channelMaskId;
    }

    public void setChannelMaskId(int channelMaskId) {
        this.channelMaskId = channelMaskId;
    }

    public int getTargetMeetup() {
        return targetMeetup;
    }

    public void setTargetMeetup(int targetMeetup) {
        this.targetMeetup = targetMeetup;
    }

    public int getTargetChannel() {
        return targetChannel;
    }

    public void setTargetChannel(int targetChannel) {
        this.targetChannel = targetChannel;
    }

    public int getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(int targetUser) {
        this.targetUser = targetUser;
    }

    public LocationRequest getPostedLocation() {
        return postedLocation;
    }

    public void setPostedLocation(LocationRequest postedLocation) {
        this.postedLocation = postedLocation;
    }

    public LocationRequest getTaggedLocation() {
        return taggedLocation;
    }

    public void setTaggedLocation(LocationRequest taggedLocation) {
        this.taggedLocation = taggedLocation;
    }

    public LocationRequest getCheckinLocation() {
        return checkinLocation;
    }

    public void setCheckinLocation(LocationRequest checkinLocation) {
        this.checkinLocation = checkinLocation;
    }
}
