package com.hoocons.hoocons_android.Models;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Networking.Responses.MediaResponse;

import java.util.List;

/**
 * Created by hungnguyen on 7/30/17.
 */

public class SimpleMeetout {
    @SerializedName("id")
    private int id;
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
    @SerializedName("meetup_location_name")
    private String meetupLocationName;
    @SerializedName("meetup_location_address")
    private String meetupLocationAddress;

    public SimpleMeetout(int id, String name, String description,
                         List<MediaResponse> promotedMedias, String fromDateTime,
                         String toDateTime, String city, String country,
                         String privacy, String meetupLocationName,
                         String meetupLocationAddress) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.promotedMedias = promotedMedias;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.city = city;
        this.country = country;
        this.privacy = privacy;
        this.meetupLocationName = meetupLocationName;
        this.meetupLocationAddress = meetupLocationAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
