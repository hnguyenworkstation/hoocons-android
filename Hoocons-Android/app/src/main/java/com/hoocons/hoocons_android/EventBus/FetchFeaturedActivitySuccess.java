package com.hoocons.hoocons_android.EventBus;

import com.hoocons.hoocons_android.Networking.Responses.ActivityResponse;

import java.util.List;

/**
 * Created by hungnguyen on 8/11/17.
 */

public class FetchFeaturedActivitySuccess {
    private List<ActivityResponse> activityResponses;

    public FetchFeaturedActivitySuccess(List<ActivityResponse> activityResponses) {
        this.activityResponses = activityResponses;
    }

    public List<ActivityResponse> getActivityResponses() {
        return activityResponses;
    }

    public void setActivityResponses(List<ActivityResponse> activityResponses) {
        this.activityResponses = activityResponses;
    }
}
