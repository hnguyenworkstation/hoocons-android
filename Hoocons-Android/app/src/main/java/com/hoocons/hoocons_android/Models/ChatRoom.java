package com.hoocons.hoocons_android.Models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hungnguyen on 8/6/17.
 */
@IgnoreExtraProperties
public class ChatRoom implements Serializable {
    @SerializedName("room_name")
    private String roomName;
    @SerializedName("room_type")
    private String roomType;

    private String uid;
    private ChatMessage lastMessage;
    private String roomProfileUrl;

    public ChatRoom() {

    }

    public ChatRoom(String roomType) {
        this.roomType = roomType;
    }

    public ChatRoom(String roomName, String roomType) {
        this.roomName = roomName;
        this.roomType = roomType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRoomProfileUrl() {
        return roomProfileUrl;
    }

    public void setRoomProfileUrl(String roomProfileUrl) {
        this.roomProfileUrl = roomProfileUrl;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public ChatMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
    }
}
