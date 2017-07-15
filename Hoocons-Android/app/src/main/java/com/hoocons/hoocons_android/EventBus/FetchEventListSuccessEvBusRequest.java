package com.hoocons.hoocons_android.EventBus;

import com.hoocons.hoocons_android.Networking.Responses.EventResponse;

import java.util.List;

/**
 * Created by hungnguyen on 7/15/17.
 */

public class FetchEventListSuccessEvBusRequest {
    private List<EventResponse> responseList;

    public FetchEventListSuccessEvBusRequest(List<EventResponse> list) {
        this.responseList = list;
    }

    public List<EventResponse> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<EventResponse> responseList) {
        this.responseList = responseList;
    }
}
