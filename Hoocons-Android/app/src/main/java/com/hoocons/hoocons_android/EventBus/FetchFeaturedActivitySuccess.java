package com.hoocons.hoocons_android.EventBus;

import com.hoocons.hoocons_android.Networking.ApiViewSets.ActivityApiViewSet;
import com.hoocons.hoocons_android.Networking.Responses.ActivityResponse;

import java.util.List;

/**
 * Created by hungnguyen on 8/11/17.
 */

public class FetchFeaturedActivitySuccess {
    private ActivityApiViewSet activityResponse;

    public FetchFeaturedActivitySuccess(ActivityApiViewSet activityResponses) {
        this.activityResponse = activityResponses;
    }

    public ActivityApiViewSet getActivityResponse() {
        return activityResponse;
    }

    public void setActivityResponse(ActivityApiViewSet activityResponses) {
        this.activityResponse = activityResponses;
    }
}
