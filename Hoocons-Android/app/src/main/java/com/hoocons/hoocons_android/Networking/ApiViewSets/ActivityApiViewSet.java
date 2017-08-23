package com.hoocons.hoocons_android.Networking.ApiViewSets;

import android.app.Activity;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Networking.Responses.ActivityResponse;

import java.util.List;

/**
 * Created by hungnguyen on 8/23/17.
 */

public class ActivityApiViewSet {
    @SerializedName("count")
    private int count;
    @SerializedName("next")
    private String next;
    @SerializedName("previous")
    private String previous;
    @SerializedName("results")
    private List<ActivityResponse> results;

    public ActivityApiViewSet(int count, String next, String previous, List<ActivityResponse> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<ActivityResponse> getResults() {
        return results;
    }

    public void setResults(List<ActivityResponse> results) {
        this.results = results;
    }
}
