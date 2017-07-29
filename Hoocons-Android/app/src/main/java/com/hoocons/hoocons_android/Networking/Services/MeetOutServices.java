package com.hoocons.hoocons_android.Networking.Services;

import com.hoocons.hoocons_android.Networking.Requests.EventInfoRequest;
import com.hoocons.hoocons_android.Networking.Requests.MeetOutRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by hungnguyen on 7/29/17.
 */

public interface MeetOutServices {
    /**********************************
     * @Method: POST
     * @Name: postNewMeetOut
     * @Argument: MeetOutRequest
     * @Purpose: post a new meetout
     ********************************** */
    @POST("/meetup/create/")
    Call<Void> postNewMeetOut(@Body MeetOutRequest request);
}
