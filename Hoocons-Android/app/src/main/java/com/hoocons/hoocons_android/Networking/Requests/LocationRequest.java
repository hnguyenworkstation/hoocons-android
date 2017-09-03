package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hungnguyen on 9/3/17.
 */

public class LocationRequest {
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("location_name")
    private String locationName;
    @SerializedName("city")
    private String city;
    @SerializedName("province")
    private String province;
    @SerializedName("state")
    private String state;
    @SerializedName("zipcode")
    private int zipcode;
    @SerializedName("country")
    private String country;

    public LocationRequest(double longitude, double latitude, String locationName,
                           String city, String province, String state,
                           int zipcode, String country) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationName = locationName;
        this.city = city;
        this.province = province;
        this.state = state;
        this.zipcode = zipcode;
        this.country = country;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
