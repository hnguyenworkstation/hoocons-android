package com.hoocons.hoocons_android.EventBus;

import com.hoocons.hoocons_android.Networking.Responses.UserInfoResponse;

/**
 * Created by hungnguyen on 7/14/17.
 */

public class FetchUserInfoCompleteEvBusRequest {
    private UserInfoResponse mResponse;

    public FetchUserInfoCompleteEvBusRequest(UserInfoResponse mResponse) {
        this.mResponse = mResponse;
    }

    public UserInfoResponse getmResponse() {
        return mResponse;
    }

    public void setmResponse(UserInfoResponse mResponse) {
        this.mResponse = mResponse;
    }
}
