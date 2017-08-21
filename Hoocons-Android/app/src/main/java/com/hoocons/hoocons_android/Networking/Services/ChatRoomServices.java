package com.hoocons.hoocons_android.Networking.Services;

import com.hoocons.hoocons_android.Networking.Requests.ChannelRequest;
import com.hoocons.hoocons_android.Networking.Requests.ChatRoomRequest;
import com.hoocons.hoocons_android.Networking.Responses.ChatRoomResponse;
import com.hoocons.hoocons_android.Networking.Responses.IdResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by hungnguyen on 8/21/17.
 */

public interface ChatRoomServices {
    /**********************************
     * @Method: POST
     * @Name: createChatRoom
     * @Argument: ChatRoomRequest
     * @Purpose: post a new chatroom
     ********************************** */
    @POST("/chatroom/create/")
    Call<IdResponse> createChatRoom(@Body ChatRoomRequest request);


    /**********************************
     * @Method: GET
     * @Name: getCurrentChatRooms
     * @Argument: Void
     * @Purpose: get all chat rooms that I am in
     ********************************** */
    @GET("/chatroom/")
    Call<List<ChatRoomResponse>> getCurrentChatRooms();
}
