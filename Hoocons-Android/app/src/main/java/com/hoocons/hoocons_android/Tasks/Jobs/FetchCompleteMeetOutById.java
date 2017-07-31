package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.BadRequest;
import com.hoocons.hoocons_android.EventBus.FetchCompleteMeetOutFailure;
import com.hoocons.hoocons_android.EventBus.FetchCompleteMeetOutSuccess;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Responses.CompleteMeetOutResponse;
import com.hoocons.hoocons_android.Networking.Services.MeetOutServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 7/31/17.
 */

public class FetchCompleteMeetOutById extends Job {
    private int id;

    public FetchCompleteMeetOutById(int id) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.meetout));
        this.id = id;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        MeetOutServices services = NetContext.instance.create(MeetOutServices.class);
        services.getMeetOutById(id).enqueue(new Callback<CompleteMeetOutResponse>() {
            @Override
            public void onResponse(Call<CompleteMeetOutResponse> call, Response<CompleteMeetOutResponse> response) {
                if (response.code() == 200) {
                    EventBus.getDefault().post(new FetchCompleteMeetOutSuccess(response.body()));
                } else {
                    EventBus.getDefault().post(new FetchCompleteMeetOutFailure());
                }
            }

            @Override
            public void onFailure(Call<CompleteMeetOutResponse> call, Throwable t) {
                EventBus.getDefault().post(new BadRequest());
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
