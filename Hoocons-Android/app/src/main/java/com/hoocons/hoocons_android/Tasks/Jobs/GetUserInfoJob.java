package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.FetchUserInfoCompleteEvBusRequest;
import com.hoocons.hoocons_android.EventBus.JobFailureEvBusRequest;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Responses.UserInfoResponse;
import com.hoocons.hoocons_android.Networking.Services.UserServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 7/14/17.
 */

public class GetUserInfoJob extends Job {
    private int userId;

    public GetUserInfoJob(int userId) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.userInfo));
        this.userId = userId;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        UserServices services = NetContext.instance.create(UserServices.class);
        services.getUserInfo(userId).enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.code() == 200) {
                    // Success
                    EventBus.getDefault().post(new FetchUserInfoCompleteEvBusRequest(response.body()));
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                EventBus.getDefault().post(new JobFailureEvBusRequest());
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
