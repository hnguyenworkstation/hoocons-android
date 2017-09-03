package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hungnguyen on 7/25/17.
 */

public class FCMTokenRequest {
    @SerializedName("old_fcm_token")
    private String oldFcmToken;
    @SerializedName("new_fcm_token")
    private String newFcmToken;

    public FCMTokenRequest(String oldFcmToken, String newFcmToken) {
        this.newFcmToken = newFcmToken;
        this.oldFcmToken = oldFcmToken;
    }

    public String getOldFcmToken() {
        return oldFcmToken;
    }

    public void setOldFcmToken(String oldFcmToken) {
        this.oldFcmToken = oldFcmToken;
    }

    public String getNewFcmToken() {
        return newFcmToken;
    }

    public void setNewFcmToken(String newFcmToken) {
        this.newFcmToken = newFcmToken;
    }
}
