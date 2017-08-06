package com.hoocons.hoocons_android.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hungnguyen on 8/6/17.
 */

public class ChatRoom implements Serializable {
    @SerializedName("users")
    private List<ChatUser> users;
    @SerializedName("room_type")
    private String roomType;

    public ChatRoom(List<ChatUser> users, String roomType) {
        this.users = users;
        this.roomType = roomType;
    }

    public List<ChatUser> getUsers() {
        return users;
    }

    public void setUsers(List<ChatUser> users) {
        this.users = users;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
}
