package com.hoocons.hoocons_android.Networking.Services;

import com.hoocons.hoocons_android.Networking.Requests.EventInfoRequest;
import com.hoocons.hoocons_android.Networking.Responses.ActivityResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

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
    @GET("/activity/get/")
    Call<List<ActivityResponse>> getActivities();
}
