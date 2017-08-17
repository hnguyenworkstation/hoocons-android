package com.hoocons.hoocons_android.EventBus;

/**
 * Created by hungnguyen on 6/17/17.
 */

public class TaskCompleteRequest {
    private String tag;
    private int id;

    public TaskCompleteRequest() {
    }

    public TaskCompleteRequest(String tag) {
        this.tag = tag;
    }

    public TaskCompleteRequest(String tag, int id) {
        this.tag = tag;
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
