package com.hoocons.hoocons_android.Tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.hoocons.hoocons_android.EventBus.LoginFailedRequest;
import com.hoocons.hoocons_android.EventBus.NewUserRequest;
import com.hoocons.hoocons_android.EventBus.TaskCancelledRequest;
import com.hoocons.hoocons_android.EventBus.UserInfoRequest;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.CredentialRequest;
import com.hoocons.hoocons_android.Networking.Responses.TokenResponse;
import com.hoocons.hoocons_android.Networking.Responses.UserInfoResponse;
import com.hoocons.hoocons_android.Networking.Services.UserServices;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 6/17/17.
 */

public class LoginAndCheckUserInfoTask extends AsyncTask<String, String, String> {
    private String username;
    private String password;
    private String token;

    public LoginAndCheckUserInfoTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected void onPostExecute(String s) {
        if (SharedPreferencesManager.getDefault().getUserToken() == null) {
            checkAvailability();
        } else {
            retrieveUserInfo();
        }
    }

    @Override
    protected void onCancelled() {
        EventBus.getDefault().post(new TaskCancelledRequest());
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            if (!username.isEmpty() && !password.isEmpty()) {
                attemptToLogin(username, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private void attemptToLogin(String phoneNumber, String password) {
        UserServices services = NetContext.instance.create(UserServices.class);
        services.login(new CredentialRequest(phoneNumber, password)).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.code() == 200) {
                    SharedPreferencesManager.getDefault().setUserToken(response.body().getAccessToken());
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {

            }
        });
    }


    private void retrieveUserInfo() {
        UserServices services =  NetContext.instance.create(UserServices.class);
        services.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.code() == 200) {
                    UserInfoResponse resp = response.body();

                    if (resp != null) {
                        SharedPreferencesManager.getDefault().setUserName(resp.getUsername());

                        if (resp.getUsername().equals(resp.getNickname())) {
                            EventBus.getDefault().post(new UserInfoRequest());
                        } else {
                            // Todo: update infomation here
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {

            }
        });
    }

    private void checkAvailability() {
        UserServices services =  NetContext.instance.create(UserServices.class);
        services.checkUsernameAvailability(new CredentialRequest(username)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    EventBus.getDefault().post(new NewUserRequest());
                } else {
                    EventBus.getDefault().post(new LoginFailedRequest());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
