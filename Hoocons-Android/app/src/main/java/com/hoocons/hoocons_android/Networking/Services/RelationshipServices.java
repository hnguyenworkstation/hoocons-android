package com.hoocons.hoocons_android.Networking.Services;

import com.hoocons.hoocons_android.Networking.ApiViewSets.FriendshipRequestApiViewSet;
import com.hoocons.hoocons_android.Networking.ApiViewSets.UserApiViewSet;
import com.hoocons.hoocons_android.Networking.Responses.RelationshipResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hungnguyen on 8/24/17.
 */

public interface RelationshipServices {
    /**********************************
     * @Method: GET
     * @Name: getFriendList
     * @Argument: none
     * @Purpose: get my friends
     ********************************** */
    @GET("/api/v1/relationship/friends/")
    Call<List<RelationshipResponse>> getFriendList();


    /**********************************
     * @Method: GET
     * @Name: getBlocksList
     * @Argument: none
     * @Purpose: get my blocks user
     ***********************************/
    @GET("/api/v1/relationship/blocks/")
    Call<List<RelationshipResponse>> getBlocksList();


    /**********************************
     * @Method: POST
     * @Name: sendFriendRequest
     * @Argument: user id
     * @Purpose: send friend request to user
     ********************************** */
    @POST("/api/v1/user/{id}/makefriend/")
    Call<Void> sendFriendRequest(@Path("id") int userId);


    /**********************************
     * @Method: DELETE
     * @Name: sendFriendRequest
     * @Argument: user id
     * @Purpose: send friend request to user
     ********************************** */
    @DELETE("/api/v1/user/{id}/makefriend/")
    Call<Void> unFriendRequest(@Path("id") int userId);


    /**********************************
     * @Method: GET
     * @Name: getSemiFriendRequest
     * @Argument: none
     * @Purpose: get friend requests that sent to me but smaller size
     ********************************** */
    @GET("/api/v1/friend/request/semi/")
    Call<FriendshipRequestApiViewSet> getSemiFriendRequest();


    /**********************************
     * @Method: GET
     * @Name: getFriendRequest
     * @Argument: none
     * @Purpose: get friends list
     ********************************** */
    @GET("/api/v1/friend/request/")
    Call<FriendshipRequestApiViewSet> getFriendRequest();


    /**********************************
     * @Method: PUT
     * @Name: acceptFriendRequest
     * @Argument: int requestId
     * @Purpose: accept friend request
     ********************************** */
    @PUT("/api/v1/friend/request/{id}/")
    Call<Void> acceptFriendRequest(@Path("id") int requestId);


    /**********************************
     * @Method: DELETE
     * @Name: declineFriendRequest
     * @Argument: int requestId
     * @Purpose: deny friend request
     ********************************** */
    @DELETE("/api/v1/friend/request/{id}/")
    Call<Void> declineFriendRequest(@Path("id") int requestId);

}
