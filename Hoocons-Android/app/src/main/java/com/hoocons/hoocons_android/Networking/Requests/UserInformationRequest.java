package com.hoocons.hoocons_android.Networking.Requests;

import com.google.gson.annotations.SerializedName;
import com.hoocons.hoocons_android.Networking.Services.UserServices;

/**
 * Created by hungnguyen on 6/17/17.
 */

public class UserInformationRequest {
    @SerializedName("nickname")
    private String nickname;

    public UserInformationRequest(String nickname) {
        this.nickname = nickname;
    }
}
