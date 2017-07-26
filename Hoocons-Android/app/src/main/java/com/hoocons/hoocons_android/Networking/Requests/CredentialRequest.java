package com.hoocons.hoocons_android.Networking.Requests;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;

/**
 * Created by hungnguyen on 6/16/17.
 */

public class CredentialRequest {
    @SerializedName("username")
    private String userName;
    @SerializedName("password")
    private String passWord;
    @SerializedName("fcm_token")
    private String fcmToken;

    public CredentialRequest(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        this.fcmToken = SharedPreferencesManager.getDefault().getFcmToken();

        if (this.fcmToken == null) {
            this.fcmToken = FirebaseInstanceId.getInstance().getToken();
            SharedPreferencesManager.getDefault().setFcmToken(fcmToken);
        }
    }

    public CredentialRequest(String userName) {
        this.userName = userName;
    }
}
