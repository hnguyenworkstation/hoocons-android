package com.hoocons.hoocons_android.EventBus;

/**
 * Created by hungnguyen on 8/25/17.
 */

public class JobAddedToDisk {
    private String tag;

    public JobAddedToDisk(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
