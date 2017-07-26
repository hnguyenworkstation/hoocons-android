package com.hoocons.hoocons_android.Networking.Services;

import com.hoocons.hoocons_android.Networking.Requests.UserInformationRequest;
import com.hoocons.hoocons_android.Networking.Responses.GooglePlaceResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hungnguyen on 7/26/17.
 */

public interface GooglePlaceServices {
    @GET("/placeholder={placeholder}/")
    Call<GooglePlaceResponse> getAroundPlaces(@Path("placeholder") String placeHolder);
}
