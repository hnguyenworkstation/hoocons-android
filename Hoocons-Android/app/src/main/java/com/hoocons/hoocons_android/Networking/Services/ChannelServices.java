package com.hoocons.hoocons_android.Networking.Services;

import com.hoocons.hoocons_android.Networking.ApiViewSets.ChannelApiViewSet;
import com.hoocons.hoocons_android.Networking.Requests.ChannelRequest;
import com.hoocons.hoocons_android.Networking.Requests.EventInfoRequest;
import com.hoocons.hoocons_android.Networking.Responses.IdResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by hungnguyen on 8/16/17.
 */

public interface ChannelServices {
    /**********************************
     * @Method: POST
     * @Name: postNewChannel
     * @Argument: ChannelRequest
     * @Purpose: post a new channel
     ********************************** */
    @POST("/api/v1/channel/")
    Call<IdResponse> postNewChannel(@Body ChannelRequest request);


    /**********************************
     * @Method: GET
     * @Name: getCreatedChannel
     * @Argument: none
     * @Purpose: get all the channel user has created
     ********************************** */
    @GET("/api/v1/channel/")
    Call<ChannelApiViewSet> getOwnedChannel();
}
