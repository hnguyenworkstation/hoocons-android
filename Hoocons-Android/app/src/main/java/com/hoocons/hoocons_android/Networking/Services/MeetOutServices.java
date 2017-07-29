package com.hoocons.hoocons_android.Networking.Services;

import com.hoocons.hoocons_android.Networking.Requests.EventInfoRequest;
import com.hoocons.hoocons_android.Networking.Requests.MeetOutRequest;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;
import com.hoocons.hoocons_android.Networking.Responses.MeetOutResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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


    /**********************************
     * @Method: GET
     * @Name: getCreatedMeetOut
     * @Argument: start and end (limits)
     * @Purpose: get all created meetout
     ********************************** */
    @GET("/meetout/get/created/start={start}/end={end}/")
    Call<List<MeetOutResponse>> getCreatedMeetOut(@Path("start") int start, @Path("end") int end);
}
