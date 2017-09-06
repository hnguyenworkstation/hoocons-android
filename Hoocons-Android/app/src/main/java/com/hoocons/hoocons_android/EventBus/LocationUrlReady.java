package com.hoocons.hoocons_android.EventBus;

/**
 * Created by hungnguyen on 9/5/17.
 */

public class LocationUrlReady {
    private String url;

    public LocationUrlReady(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
