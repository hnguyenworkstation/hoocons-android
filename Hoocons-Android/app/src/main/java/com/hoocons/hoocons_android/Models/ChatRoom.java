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

    public ChatRoom() {
    }

    public ChatRoom(String roomType) {
        this.roomType = roomType;
    }

    public ChatRoom(String roomName, String roomType) {
        this.roomName = roomName;
        this.roomType = roomType;
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
}
