package com.hoocons.hoocons_android.Networking.Services;

import com.hoocons.hoocons_android.Networking.Requests.EventInfoRequest;
import com.hoocons.hoocons_android.Networking.Requests.UserInformationRequest;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by hungnguyen on 7/12/17.
 */

public interface EventServices {
    /**********************************
     * @Method: POST
     * @Name: postEvent
     * @Argument: EventInfoRequest
     * @Purpose: post a new event
     ********************************** */
    @POST("/event/create/")
    Call<Void> postEvent(@Body EventInfoRequest request);


    /**********************************
     * @Method: GET
     * @Name: postEvent
     * @Argument: EventInfoRequest
     * @Purpose: post a new event
     ********************************** */
    @GET("/event/get/created/start={start}/end={end}/")
    Call<List<EventResponse>> getCreatedEvent(@Path("start") int start, @Path("end") int end);
}
