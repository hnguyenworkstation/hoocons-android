package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hungnguyen on 7/10/17.
 */
@Parcel
public class CoordinateResponse implements Serializable{
    @SerializedName("srid")
    int srid;
    @SerializedName("latitude")
    double latitude;
    @SerializedName("longitude")
    double longitude;

    public CoordinateResponse() {
    }

    public CoordinateResponse(int srid, double latitude, double longitude) {
        this.srid = srid;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CoordinateResponse(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getSrid() {
        return srid;
    }

    public void setSrid(int srid) {
        this.srid = srid;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
