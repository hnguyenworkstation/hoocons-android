package com.hoocons.hoocons_android.EventBus;

import com.hoocons.hoocons_android.Networking.Requests.LocationRequest;

/**
 * Created by hungnguyen on 9/8/17.
 */

public class LocationRequestCollected {
    private LocationRequest request;
    private String tag;

    public LocationRequestCollected(LocationRequest request, String tag) {
        this.request = request;
        this.tag = tag;
    }

    public LocationRequest getRequest() {
        return request;
    }

    public void setRequest(LocationRequest request) {
        this.request = request;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
