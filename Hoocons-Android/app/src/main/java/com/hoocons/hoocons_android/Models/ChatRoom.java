package com.hoocons.hoocons_android.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hungnguyen on 8/6/17.
 */

public class ChatRoom implements Serializable {
    @SerializedName("room_id")
    private String roomId;
    @SerializedName("room_type")
    private String roomType;

    public ChatRoom() {
    }

    public ChatRoom(String roomType) {
        this.roomType = roomType;
    }

    public ChatRoom(String roomId, String roomType, String firstUserId, String secondUserId) {
        this.roomId = roomId;
        this.roomType = roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
}
