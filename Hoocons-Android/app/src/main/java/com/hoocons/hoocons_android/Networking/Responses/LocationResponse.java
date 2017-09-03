package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by hungnguyen on 9/3/17.
 */
@Parcel
public class LocationResponse {
    @SerializedName("coordinate")
    CoordinateResponse coordinate;
    @SerializedName("location_name")
    String locationName;
    @SerializedName("city")
    String city;
    @SerializedName("province")
    String province;
    @SerializedName("state")
    String state;
    @SerializedName("country")
    String country;
    @SerializedName("zipcode")
    int zipcode;

    public LocationResponse() {
    }

    public LocationResponse(CoordinateResponse coordinate, String locationName, String city,
                            String province, String state, String country, int zipcode) {
        this.coordinate = coordinate;
        this.locationName = locationName;
        this.city = city;
        this.province = province;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
    }

    public CoordinateResponse getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(CoordinateResponse coordinate) {
        this.coordinate = coordinate;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }
}
