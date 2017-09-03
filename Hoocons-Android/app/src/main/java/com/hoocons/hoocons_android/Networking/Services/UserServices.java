package com.hoocons.hoocons_android.Networking.Services;

import com.hoocons.hoocons_android.EventBus.UserInfoRequest;
import com.hoocons.hoocons_android.Networking.Requests.CredentialRequest;
import com.hoocons.hoocons_android.Networking.Requests.FCMTokenRequest;
import com.hoocons.hoocons_android.Networking.Requests.UserInformationRequest;
import com.hoocons.hoocons_android.Networking.Responses.TokenResponse;
import com.hoocons.hoocons_android.Networking.Responses.UserInfoResponse;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by hungnguyen on 6/16/17.
 */
public interface UserServices {
    /**********************************
     * @Method: POST
     * @Name: Register
     * @Argument: both credentials
     * @Purpose: Login and receive token
     ********************************** */
    @POST("/api/v1/register/")
    Call<TokenResponse> register(@Body CredentialRequest request);

    /**********************************
     * @Method: POST
     * @Name: Login
     * @Argument: both credentials
     * @Purpose: Login and receive token
     ********************************** */
    @POST("/api/v1/login/")
    Call<TokenResponse> login(@Body CredentialRequest request);

    /**********************************
     * @Method: POST
     * @Name: checkUsernameAvailability
     * @Argument: username
     * @Purpose: check if the username has been registered or not
     ********************************** */
    @POST("/api/v1/info/check_username/")
    Call<Void> checkUsernameAvailability(@Body CredentialRequest request);


    /**********************************
     * @Method: POST
     * @Name: checkNicknameAvailability
     * @Argument: username
     * @Purpose: check if the nickname has been registered or not
     ********************************** */
    @POST("/api/v1/info/check_nickname/")
    Call<Void> checkNicknameAvailability(@Body UserInformationRequest request);

    /**********************************
     * @Method: GET
     * @Name: getUserInfo
     * @Argument: void
     * @Purpose: retrieve user info from backend
     ********************************** */
    @GET("/api/v1/user/{id}/")
    Call<UserInfoResponse> getUserInfo(@Path("id") int id);

    /**********************************
     * @Method: PUT
     * @Name: updateUserInfo
     * @Argument: UserInfoResponse
     * @Purpose: update user data in the database
     ********************************** */
    @PUT("/api/v1/user/{id}/")
    Call<Void> updateUserInfo(@Path("id") int id, @Body UserInformationRequest request);


    /**********************************
     * @Method: PUT
     * @Name: updateFcmToken
     * @Argument: new token
     * @Purpose: update GCM token from server
     ********************************** */
    @PATCH("/api/v1/user/{id}/update_fcm")
    Call<Void> updateFcmToken(@Body FCMTokenRequest request);

}
