package com.hoocons.hoocons_android.Networking.Services;

import com.hoocons.hoocons_android.Networking.Requests.CredentialRequest;
import com.hoocons.hoocons_android.Networking.Responses.TokenResponse;
import com.hoocons.hoocons_android.Networking.Responses.UserInfoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by hungnguyen on 6/16/17.
 */
public interface UserServices {
    @POST("/login")
    Call<TokenResponse> login(@Body CredentialRequest request);
    @POST("/api/user/availability")
    Call<Void> checkUsernameAvailability(@Body CredentialRequest request);


    @GET("/api/user/get/info")
    Call<UserInfoResponse> getUserInfo();
}
