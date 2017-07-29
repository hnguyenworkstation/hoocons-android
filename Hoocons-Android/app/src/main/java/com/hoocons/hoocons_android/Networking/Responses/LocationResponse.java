package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by hungnguyen on 7/10/17.
 */
@Parcel
public class LocationResponse {
    @SerializedName("srid")
    int srid;
    @SerializedName("coordinates")
    double[] coordinates;

    public LocationResponse() {
    }

    public int getSrid() {
        return srid;
    }

    public void setSrid(int srid) {
        this.srid = srid;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
