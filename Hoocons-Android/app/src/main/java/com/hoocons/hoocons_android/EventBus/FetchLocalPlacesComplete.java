package com.hoocons.hoocons_android.EventBus;

import com.hoocons.hoocons_android.Networking.Responses.GooglePlaceResponse;

/**
 * Created by hungnguyen on 7/26/17.
 */

public class FetchLocalPlacesComplete {
    private GooglePlaceResponse response;

    public FetchLocalPlacesComplete(GooglePlaceResponse response) {
        this.response = response;
    }

    public GooglePlaceResponse getResponse() {
        return response;
    }

    public void setResponse(GooglePlaceResponse response) {
        this.response = response;
    }
}
