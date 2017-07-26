package com.hoocons.hoocons_android.Networking.Services;

import com.hoocons.hoocons_android.Networking.Requests.UserInformationRequest;
import com.hoocons.hoocons_android.Networking.Responses.GooglePlaceResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hungnguyen on 7/26/17.
 */

public interface GooglePlaceServices {
    @GET("/maps/api/place/nearbysearch/json")
    Call<GooglePlaceResponse> getAroundPlaces(@Query("location") String location,
                                              @Query("radius") String radius,
                                              @Query("type") String type,
                                              @Query("key") String keyApi);

    @GET("/maps/api/place/nearbysearch/json")
    Call<GooglePlaceResponse> getNextAroundPlaces(@Query("key") String keyApi,
                                              @Query("pagetoken") String placeHolder);
}
