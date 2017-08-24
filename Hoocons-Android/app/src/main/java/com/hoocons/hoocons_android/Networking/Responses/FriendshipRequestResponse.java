package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hungnguyen on 8/24/17.
 */

public class FriendshipRequestResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("user")
    private SemiUserInfoResponse user;
    @SerializedName("request_made_at")
    private String requestMadeAt;
    @SerializedName("message")
    private String message;
    @SerializedName("found_user_from")
    private String foundUserFrom;

    public FriendshipRequestResponse(int id, SemiUserInfoResponse user,
                                     String requestMadeAt, String message, String foundUserFrom) {
        this.id = id;
        this.user = user;
        this.requestMadeAt = requestMadeAt;
        this.message = message;
        this.foundUserFrom = foundUserFrom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SemiUserInfoResponse getUser() {
        return user;
    }

    public void setUser(SemiUserInfoResponse user) {
        this.user = user;
    }

    public String getRequestMadeAt() {
        return requestMadeAt;
    }

    public void setRequestMadeAt(String requestMadeAt) {
        this.requestMadeAt = requestMadeAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFoundUserFrom() {
        return foundUserFrom;
    }

    public void setFoundUserFrom(String foundUserFrom) {
        this.foundUserFrom = foundUserFrom;
    }
}
