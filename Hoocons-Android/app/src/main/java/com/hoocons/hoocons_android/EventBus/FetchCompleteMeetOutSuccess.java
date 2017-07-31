package com.hoocons.hoocons_android.EventBus;

import com.hoocons.hoocons_android.Networking.Responses.CompleteMeetOutResponse;

/**
 * Created by hungnguyen on 7/31/17.
 */

public class FetchCompleteMeetOutSuccess {
    private CompleteMeetOutResponse response;

    public FetchCompleteMeetOutSuccess(CompleteMeetOutResponse response) {
        this.response = response;
    }

    public CompleteMeetOutResponse getResponse() {
        return response;
    }

    public void setResponse(CompleteMeetOutResponse response) {
        this.response = response;
    }
}
