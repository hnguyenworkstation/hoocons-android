package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hungnguyen on 8/21/17.
 */

public class ChatRoomResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("users")
    private List<SemiUserInfoResponse> users;
    @SerializedName("uid")
    private String uid;

    public ChatRoomResponse() {
    }

    public ChatRoomResponse(int id, List<SemiUserInfoResponse> users, String uid) {
        this.id = id;
        this.users = users;
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<SemiUserInfoResponse> getUsers() {
        return users;
    }

    public void setUsers(List<SemiUserInfoResponse> users) {
        this.users = users;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
