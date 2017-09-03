package com.hoocons.hoocons_android.Tasks;

import android.os.AsyncTask;

import com.hoocons.hoocons_android.EventBus.BadRequest;
import com.hoocons.hoocons_android.EventBus.FieldAvailableRequest;
import com.hoocons.hoocons_android.EventBus.FieldUnavailableRequest;
import com.hoocons.hoocons_android.EventBus.ServerErrorRequest;
import com.hoocons.hoocons_android.EventBus.UserInfoRequest;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.UserInformationRequest;
import com.hoocons.hoocons_android.Networking.Services.UserServices;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 6/17/17.
 */
public class CheckNicknameAvailabilityTask extends AsyncTask<String, String, String> {
    private String nickname;

    public CheckNicknameAvailabilityTask(String nickname) {
        this.nickname = nickname;
    }

    @Override
    protected String doInBackground(String... params) {
        if (nickname != null && nickname.length() > 5) {
            checkAvailability(nickname);
        }

        return null;
    }

    private void checkAvailability(String nickname) {
        UserServices services = NetContext.instance.create(UserServices.class);
        services.checkNicknameAvailability(new UserInformationRequest(nickname))
                .enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() ==  200) {
                    EventBus.getDefault().post(new FieldAvailableRequest());
                } else if (response.code() == 201) {
                    EventBus.getDefault().post(new FieldUnavailableRequest());
                } else {
                    EventBus.getDefault().post(new BadRequest());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                EventBus.getDefault().post(new ServerErrorRequest());
            }
        });
    }
}
