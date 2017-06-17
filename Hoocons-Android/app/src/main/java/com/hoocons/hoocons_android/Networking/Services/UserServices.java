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
    @POST("/login")
    Call<TokenResponse> login(@Body CredentialRequest request);

    /**********************************
     * @Method: POST
     * @Name: checkUsernameAvailability
     * @Argument: username
     * @Purpose: check if the username has been registered or not
     ********************************** */
    @POST("/api/user/username/check")
    Call<Void> checkUsernameAvailability(@Body CredentialRequest request);


    /**********************************
     * @Method: POST
     * @Name: checkNicknameAvailability
     * @Argument: username
     * @Purpose: check if the nickname has been registered or not
     ********************************** */
    @POST("/api/user/nickname/check")
    Call<Void> checkNicknameAvailability(@Body UserInformationRequest request);


    /**********************************
     * @Method: GET
     * @Name: getUserInfo
     * @Argument: void
     * @Purpose: retrieve user info from backend
     ********************************** */
    @GET("/api/user/get/info")
    Call<UserInfoResponse> getUserInfo();
}
