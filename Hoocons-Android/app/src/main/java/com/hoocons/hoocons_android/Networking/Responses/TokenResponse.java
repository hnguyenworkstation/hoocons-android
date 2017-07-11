package com.hoocons.hoocons_android.Networking.Responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hungnguyen on 6/16/17.
 */

public class TokenResponse {
    @SerializedName("token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
