package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hungnguyen on 8/21/17.
 */

public class ChatRoomRequest {
    @SerializedName("users")
    private int[] userList;
    @SerializedName("uid")
    private String roomUid;

    public ChatRoomRequest(int[] userList, String roomUid) {
        this.userList = userList;
        this.roomUid = roomUid;
    }

    public int[] getUserList() {
        return userList;
    }

    public void setUserList(int[] userList) {
        this.userList = userList;
    }

    public String getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(String roomUid) {
        this.roomUid = roomUid;
    }
}
