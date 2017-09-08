package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hungnguyen on 9/8/17.
 */

public class GooglePlaceLocation {
    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
