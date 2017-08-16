package com.hoocons.hoocons_android.EventBus;

/**
 * Created by hungnguyen on 6/17/17.
 */

public class TaskCompleteRequest {
    public String tag;

    public TaskCompleteRequest() {
    }

    public TaskCompleteRequest(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
