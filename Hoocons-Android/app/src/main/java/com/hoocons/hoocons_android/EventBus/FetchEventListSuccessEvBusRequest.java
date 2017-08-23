package com.hoocons.hoocons_android.EventBus;

import com.hoocons.hoocons_android.Networking.ApiViewSets.EventsApiViewSet;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;

import java.util.List;

/**
 * Created by hungnguyen on 7/15/17.
 */

public class FetchEventListSuccessEvBusRequest {
    private EventsApiViewSet apiViewSet;

    public FetchEventListSuccessEvBusRequest(EventsApiViewSet apiViewSet) {
        this.apiViewSet = apiViewSet;
    }

    public EventsApiViewSet getApiViewSet() {
        return apiViewSet;
    }

    public void setApiViewSet(EventsApiViewSet apiViewSet) {
        this.apiViewSet = apiViewSet;
    }
}
