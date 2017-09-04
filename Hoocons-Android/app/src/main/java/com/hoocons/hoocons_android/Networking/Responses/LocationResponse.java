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
    CoordinateResponse response;
    @SerializedName("location_name")
    String locationName;
    @SerializedName("city")
    String city;
    @SerializedName("province")
    String province;
    @SerializedName("state")
    String state;
    @SerializedName("zipcode")
    int zipcode;
    @SerializedName("country")
    String country;
    @SerializedName("address")
    String address;
    @SerializedName("place_id")
    String placeId;
    @SerializedName("place_api_type")
    String placeApiType;

    public LocationResponse() {
    }

    public LocationResponse(CoordinateResponse response, String locationName, String city,
                            String province, String state, int zipcode, String country,
                            String address, String placeId, String placeApiType) {
        this.response = response;
        this.locationName = locationName;
        this.city = city;
        this.province = province;
        this.state = state;
        this.zipcode = zipcode;
        this.country = country;
        this.address = address;
        this.placeId = placeId;
        this.placeApiType = placeApiType;
    }

    public CoordinateResponse getResponse() {
        return response;
    }

    public void setResponse(CoordinateResponse response) {
        this.response = response;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceApiType() {
        return placeApiType;
    }

    public void setPlaceApiType(String placeApiType) {
        this.placeApiType = placeApiType;
    }
}
