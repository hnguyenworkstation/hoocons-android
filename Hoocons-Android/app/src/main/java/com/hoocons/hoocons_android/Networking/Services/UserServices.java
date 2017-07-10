package com.hoocons.hoocons_android.Networking.Services;

import com.hoocons.hoocons_android.EventBus.UserInfoRequest;
import com.hoocons.hoocons_android.Networking.Requests.CredentialRequest;
import com.hoocons.hoocons_android.Networking.Requests.UserInformationRequest;
import com.hoocons.hoocons_android.Networking.Responses.TokenResponse;
import com.hoocons.hoocons_android.Networking.Responses.UserInfoResponse;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by hungnguyen on 6/16/17.
 */
public interface UserServices {

    /**********************************
     * @Method: POST
     * @Name: Login
     * @Argument: both credentials
     * @Purpose: Login and receive token
     ********************************** */
    @POST("/user/login/")
    Call<TokenResponse> login(@Body CredentialRequest request);

    /**********************************
     * @Method: POST
     * @Name: checkUsernameAvailability
     * @Argument: username
     * @Purpose: check if the username has been registered or not
     ********************************** */
    @POST("/user/check/username/")
    Call<Void> checkUsernameAvailability(@Body CredentialRequest request);


    /**********************************
     * @Method: POST
     * @Name: checkNicknameAvailability
     * @Argument: username
     * @Purpose: check if the nickname has been registered or not
     ********************************** */
    @POST("/user/check/nickname/")
    Call<Void> checkNicknameAvailability(@Body UserInformationRequest request);


    /**********************************
     * @Method: GET
     * @Name: getUserInfo
     * @Argument: void
     * @Purpose: retrieve user info from backend
     ********************************** */
    @GET("/api/user/get/info")
    Call<UserInfoResponse> getUserInfo();


    /**********************************
     * @Method: PUT
     * @Name: updateUserInfo
     * @Argument: UserInfoResponse
     * @Purpose: update user data in the database
     ********************************** */
    @PUT("/api/user/update/info")
    Call<UserInfoResponse> updateUserInfo(@Body UserInformationRequest request);
}
