package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.FCMTokenRequest;
import com.hoocons.hoocons_android.Networking.Services.UserServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 7/25/17.
 */
public class UpdateFCMJob extends Job implements Serializable {
    private String oldToken;
    private String newToken;

    public UpdateFCMJob(String oldToken, String newToken) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.userInfo));

        this.oldToken = oldToken;
        this.newToken = newToken;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        UserServices services = NetContext.instance.create(UserServices.class);
        services.updateFcmToken(new FCMTokenRequest(oldToken, newToken))
                .enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
