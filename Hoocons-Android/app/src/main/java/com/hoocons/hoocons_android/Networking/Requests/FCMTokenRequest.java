package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hungnguyen on 7/25/17.
 */

public class FCMTokenRequest {
    @SerializedName("new_fcm_token")
    private String newFcmToken;

    public FCMTokenRequest(String newFcmToken) {
        this.newFcmToken = newFcmToken;
    }

    public String getNewFcmToken() {
        return newFcmToken;
    }

    public void setNewFcmToken(String newFcmToken) {
        this.newFcmToken = newFcmToken;
    }
}
