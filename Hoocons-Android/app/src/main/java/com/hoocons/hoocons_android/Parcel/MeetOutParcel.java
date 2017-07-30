package com.hoocons.hoocons_android.Parcel;

import org.parceler.Parcel;

/**
 * Created by hungnguyen on 7/30/17.
 */
@Parcel
public class MeetOutParcel {
    int id;
    String meetOutName;
    String meetOutMediaUrl;
    String ownerImageUrl;

    public MeetOutParcel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeetOutName() {
        return meetOutName;
    }

    public void setMeetOutName(String meetOutName) {
        this.meetOutName = meetOutName;
    }

    public String getMeetOutMediaUrl() {
        return meetOutMediaUrl;
    }

    public void setMeetOutMediaUrl(String meetOutMediaUrl) {
        this.meetOutMediaUrl = meetOutMediaUrl;
    }

    public String getOwnerImageUrl() {
        return ownerImageUrl;
    }

    public void setOwnerImageUrl(String ownerImageUrl) {
        this.ownerImageUrl = ownerImageUrl;
    }
}
