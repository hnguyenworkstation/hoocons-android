package com.hoocons.hoocons_android.Networking.Services;

import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Networking.ApiViewSets.EventsApiViewSet;
import com.hoocons.hoocons_android.Networking.Requests.CommentRequest;
import com.hoocons.hoocons_android.Networking.Requests.EventInfoRequest;
import com.hoocons.hoocons_android.Networking.Requests.UserInformationRequest;
import com.hoocons.hoocons_android.Networking.Responses.CommentResponse;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    @POST("/api/v1/event/")
    Call<Void> postEvent(@Body EventInfoRequest request);


    /**********************************
     * @Method: POST
     * @Name: postEvent
     * @Argument: EventInfoRequest
     * @Purpose: post a new event
     ********************************** */
    @GET("/api/v1/event/")
    Call<EventsApiViewSet> getEventPosted(@Query("page") int page);


    /**********************************
     * @Method: POST
     * @Name: likeEvent
     * @Argument: VOID
     * @Purpose: like a new event
     ********************************** */
    @POST("/api/v1/event/{id}/like/")
    Call<Void> likeEvent(@Path("id") int id);

    /**********************************
     * @Method: DELETE
     * @Name: unlikeEvent
     * @Argument: VOID
     * @Purpose: unlikeEvent a event
     ********************************** */
    @DELETE("/api/v1/event/{id}/like/")
    Call<Void> unlikeEvent(@Path("id") int id);

    /**********************************
     * @Method: POST
     * @Name: shareEvent
     * @Argument: VOID
     * @Purpose: share a new event
     ********************************** */
    @POST("/api/v1/event/{id}/share/")
    Call<Void> shareEvent(@Path("id") int id, @Body EventInfoRequest request);
    

    /**********************************
     * @Method: POST
     * @Name: postComment
     * @Argument: VOID
     * @Purpose: post new Comment to Event
     ********************************** */
    @POST("/comment/create/event_id={event_id}/")
    Call<Void> postComment(@Path("event_id") int id, @Body CommentRequest request);


    /**********************************
     * @Method: GET
     * @Name: postComment
     * @Argument: eventId, startPosition and endposition
     * @Purpose: post new Comment to Event
     ********************************** */
    @GET("/comment/get/event_id={event_id}/offset={offset}/limit={limit}/")
    Call<List<CommentResponse>> getComments(@Path("event_id") int id, @Path("offset") int offset,
                                            @Path("limit") int limit);
}
