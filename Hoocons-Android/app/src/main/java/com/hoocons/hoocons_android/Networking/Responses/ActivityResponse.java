package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Comment;

import java.util.List;

/**
 * Created by hungnguyen on 8/11/17.
 */

public class ActivityResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("actor")
    private SemiUserInfoResponse actor;
    @SerializedName("target")
    private EventResponse target;
    @SerializedName("content")
    private CommentResponse content;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("privacy")
    private String privacy;
    @SerializedName("tag")
    private String tag;

    public ActivityResponse(int id, SemiUserInfoResponse actor, EventResponse target,
                            CommentResponse content, String timestamp, String privacy, String tag) {
        this.id = id;
        this.actor = actor;
        this.target = target;
        this.content = content;
        this.timestamp = timestamp;
        this.privacy = privacy;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SemiUserInfoResponse getActor() {
        return actor;
    }

    public void setActor(SemiUserInfoResponse actor) {
        this.actor = actor;
    }

    public EventResponse getTarget() {
        return target;
    }

    public void setTarget(EventResponse target) {
        this.target = target;
    }

    public CommentResponse getContent() {
        return content;
    }

    public void setContent(CommentResponse content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
