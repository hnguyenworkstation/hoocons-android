package com.hoocons.hoocons_android.Networking.Services;

import com.hoocons.hoocons_android.Networking.Requests.EventInfoRequest;
import com.hoocons.hoocons_android.Networking.Requests.UserInformationRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

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
}
