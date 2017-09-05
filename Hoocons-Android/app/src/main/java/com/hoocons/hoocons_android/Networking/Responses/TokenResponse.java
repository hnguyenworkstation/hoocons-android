package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hungnguyen on 6/16/17.
 */

public class TokenResponse {
    @SerializedName("user_id")
    private int userId;
    @SerializedName("token")
    private String accessToken;

    public TokenResponse() {
    }

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public TokenResponse(int userId) {
        this.userId = userId;
    }

    public TokenResponse(int userId, String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
