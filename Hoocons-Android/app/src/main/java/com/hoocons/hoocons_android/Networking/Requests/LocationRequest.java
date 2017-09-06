package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Networking.Responses.CoordinateResponse;

import java.io.Serializable;

/**
 * Created by hungnguyen on 9/3/17.
 */

public class LocationRequest implements Serializable {
    @SerializedName("coordinate")
    private CoordinateResponse response;
    @SerializedName("location_name")
    private String locationName;
    @SerializedName("city")
    private String city;
    @SerializedName("province")
    private String province;
    @SerializedName("state")
    private String state;
    @SerializedName("zipcode")
    private String zipcode;
    @SerializedName("country")
    private String country;
    @SerializedName("address")
    private String address;
    @SerializedName("place_id")
    private String placeId;
    @SerializedName("place_api_type")
    private String placeApiType;

    public LocationRequest() {
    }

    public LocationRequest(CoordinateResponse response, String locationName,
                           String city, String province, String state,
                           String zipcode, String country, String address,
                           String placeId, String placeApiType) {
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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
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
