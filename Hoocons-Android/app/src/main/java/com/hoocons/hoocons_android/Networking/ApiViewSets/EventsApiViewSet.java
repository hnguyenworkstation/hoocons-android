package com.hoocons.hoocons_android.Networking.ApiViewSets;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Networking.Responses.ActivityResponse;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;

import java.util.List;

/**
 * Created by hungnguyen on 8/23/17.
 */

public class EventsApiViewSet {
    @SerializedName("count")
    private int count;
    @SerializedName("next")
    private String next;
    @SerializedName("previous")
    private String previous;
    @SerializedName("results")
    private List<EventResponse> results;

    public int getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public List<EventResponse> getResults() {
        return results;
    }
}
