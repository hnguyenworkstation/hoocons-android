package com.hoocons.hoocons_android.Networking.Responds;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hungnguyen on 6/16/17.
 */

public class TokenRespond {
    @SerializedName("access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
