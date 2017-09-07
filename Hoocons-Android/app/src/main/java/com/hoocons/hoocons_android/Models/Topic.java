package com.hoocons.hoocons_android.Models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by hungnguyen on 7/28/17.
 */
@Parcel
public class Topic {
    @SerializedName("id")
    int id;
    @SerializedName("topic_name")
    String topicName;

    public Topic() {
    }

    public Topic(String topicName) {
        this.topicName = topicName;
    }

    public Topic(int id) {
        this.id = id;
    }

    public Topic(int id, String topicName) {
        this.id = id;
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
