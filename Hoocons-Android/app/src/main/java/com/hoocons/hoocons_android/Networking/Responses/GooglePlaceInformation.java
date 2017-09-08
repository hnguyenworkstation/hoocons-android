package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hungnguyen on 9/8/17.
 */

public class GooglePlaceInformation {
    @SerializedName("results")
    private List<GooglePlaceAttributes> results;
    @SerializedName("status")
    private String status;

    public GooglePlaceInformation() {
    }

    public GooglePlaceInformation(List<GooglePlaceAttributes> results, String status) {
        this.results = results;
        this.status = status;
    }

    public List<GooglePlaceAttributes> getResults() {
        return results;
    }

    public void setResults(List<GooglePlaceAttributes> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
