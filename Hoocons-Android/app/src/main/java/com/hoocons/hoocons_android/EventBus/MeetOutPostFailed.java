package com.hoocons.hoocons_android.EventBus;

import java.util.List;

/**
 * Created by hungnguyen on 7/29/17.
 */

public class MeetOutPostFailed {
    private String meetoutName;
    private String meetoutDesc;

    private double meetingLong;
    private double meetingLat;

    private String locationName;
    private String locationAddress;
    private String fromDateTime;
    private String toDateTime;

    private List<String> imagePaths;
    private List<String> topics;

    public MeetOutPostFailed(String meetoutName, String meetoutDesc,
                             double meetingLong, double meetingLat,
                             String locationName, String locationAddress,
                             String fromDateTime, String toDateTime,
                             List<String> imagePaths, List<String> topics) {
        this.meetoutName = meetoutName;
        this.meetoutDesc = meetoutDesc;
        this.meetingLong = meetingLong;
        this.meetingLat = meetingLat;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.imagePaths = imagePaths;
        this.topics = topics;
    }

    public String getMeetoutName() {
        return meetoutName;
    }

    public void setMeetoutName(String meetoutName) {
        this.meetoutName = meetoutName;
    }

    public String getMeetoutDesc() {
        return meetoutDesc;
    }

    public void setMeetoutDesc(String meetoutDesc) {
        this.meetoutDesc = meetoutDesc;
    }

    public double getMeetingLong() {
        return meetingLong;
    }

    public void setMeetingLong(double meetingLong) {
        this.meetingLong = meetingLong;
    }

    public double getMeetingLat() {
        return meetingLat;
    }

    public void setMeetingLat(double meetingLat) {
        this.meetingLat = meetingLat;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
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

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}
