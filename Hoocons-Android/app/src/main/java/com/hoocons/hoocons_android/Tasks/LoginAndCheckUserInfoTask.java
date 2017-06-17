package com.hoocons.hoocons_android.Tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.hoocons.hoocons_android.EventBus.LoginFailedRequest;
import com.hoocons.hoocons_android.EventBus.NewUserRequest;
import com.hoocons.hoocons_android.EventBus.TaskCancelledRequest;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.CredentialRequest;
import com.hoocons.hoocons_android.Networking.Responds.TokenRespond;
import com.hoocons.hoocons_android.Networking.Services.UserServices;
import com.hoocons.hoocons_android.R;

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
        if (token == null) {
            checkAvailability();
        } else {

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
        services.login(new CredentialRequest(phoneNumber, password)).enqueue(new Callback<TokenRespond>() {
            @Override
            public void onResponse(Call<TokenRespond> call, Response<TokenRespond> response) {
                if (response.code() == 200) {
                    token = response.body().getAccessToken();
                    SharedPreferencesManager.getDefault().setUserToken(token);
                }
            }

            @Override
            public void onFailure(Call<TokenRespond> call, Throwable t) {

            }
        });
    }


    private void retrieveUserInfo() {

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
