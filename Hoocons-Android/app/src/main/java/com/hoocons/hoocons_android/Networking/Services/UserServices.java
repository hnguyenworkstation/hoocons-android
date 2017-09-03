package com.hoocons.hoocons_android.Networking.Services;

import com.google.android.gms.nearby.messages.internal.Update;
import com.hoocons.hoocons_android.EventBus.UserInfoRequest;
import com.hoocons.hoocons_android.Networking.Requests.CredentialRequest;
import com.hoocons.hoocons_android.Networking.Requests.FCMTokenRequest;
import com.hoocons.hoocons_android.Networking.Requests.HobbiesRequest;
import com.hoocons.hoocons_android.Networking.Requests.LocationRequest;
import com.hoocons.hoocons_android.Networking.Requests.ProfileMediaRequest;
import com.hoocons.hoocons_android.Networking.Requests.UpdatePasswordRequest;
import com.hoocons.hoocons_android.Networking.Requests.UserInformationRequest;
import com.hoocons.hoocons_android.Networking.Requests.WallpaperMediaRequest;
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
     * @Method: PATCH
     * @Name: updateFcmToken
     * @Argument: new token
     * @Purpose: update GCM token from server
     ********************************** */
    @PATCH("/api/v1/user/{id}/update_fcm")
    Call<Void> updateFcmToken(@Path("id") int id, @Body FCMTokenRequest request);

    /**********************************
     * @Method: PATCH
     * @Name: changePassword
     * @Argument: UpdatePasswordRequest
     * @Purpose: update password
     ********************************** */
    @PATCH("/api/v1/user/{id}/update_password/")
    Call<Void> changePassword(@Path("id") int id, @Body UpdatePasswordRequest request);

    /**********************************
     * @Method: PATCH
     * @Name: updateProfile
     * @Argument: UpdatePasswordRequest
     * @Purpose: update profile picture
     ********************************** */
    @PATCH("/api/v1/user/{id}/update_profile/")
    Call<Void> updateProfile(@Path("id") int id, @Body ProfileMediaRequest request);

    /**********************************
     * @Method: PATCH
     * @Name: updateWallpaper
     * @Argument: WallpaperMediaRequest
     * @Purpose: update wallpaper picture
     ********************************** */
    @PATCH("/api/v1/user/{id}/update_wallpaper/")
    Call<Void> updateWallpaper(@Path("id") int id, @Body WallpaperMediaRequest request);

    /**********************************
     * @Method: PATCH
     * @Name: updateHobbies
     * @Argument: user id, HobbiesRequest
     * @Purpose: update list hobbies or topics related
     ********************************** */
    @PATCH("/api/v1/user/{id}/update_hobbies/")
    Call<Void> updateHobbies(@Path("id") int id, @Body HobbiesRequest request);

    /**********************************
     * @Method: PATCH
     * @Name: updateLocation
     * @Argument: user id, LocationRequest
     * @Purpose: update user's location
     ********************************** */
    @PATCH("/api/v1/user/{id}/update_location/")
    Call<Void> updateLocation(@Path("id") int id, @Body LocationRequest request);

    /**********************************
     * @Method: PATCH
     * @Name: shareLocation
     * @Argument: user id
     * @Purpose: share user's location
     ********************************** */
    @PATCH("/api/v1/user/{id}/share_location/")
    Call<Void> shareLocation(@Path("id") int id);

    /**********************************
     * @Method: PATCH
     * @Name: clearLocation
     * @Argument: user id
     * @Purpose: clear user's location (unshare location)
     ********************************** */
    @DELETE("/api/v1/user/{id}/share_location/")
    Call<Void> clearLocation(@Path("id") int id);
}
