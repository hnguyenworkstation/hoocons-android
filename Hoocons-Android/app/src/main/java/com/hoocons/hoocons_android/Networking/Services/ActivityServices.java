package com.hoocons.hoocons_android.Networking.Services;

import com.hoocons.hoocons_android.Networking.ApiViewSets.ActivityApiViewSet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by hungnguyen on 8/11/17.
 */

public interface ActivityServices {
    /**********************************
     * @Method: GET
     * @Name: getActivities
     * @Argument: List<ActivityResponse>
     * @Purpose: getting list of activities from who we follow
     ********************************** */
    @GET("/api/v1/activity/feeds/")
    Call<ActivityApiViewSet> getPageActivities(@Query("page") int pageNum);
}
