package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hungnguyen on 6/16/17.
 */

public class CredentialRequest {
    @SerializedName("username")
    private String userName;
    @SerializedName("password")
    private String passWord;

    public CredentialRequest(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public CredentialRequest(String userName) {
        this.userName = userName;
    }
}
