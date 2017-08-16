package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.BadRequest;
import com.hoocons.hoocons_android.EventBus.JobFailureEvBusRequest;
import com.hoocons.hoocons_android.EventBus.TaskCompleteRequest;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.ChannelRequest;
import com.hoocons.hoocons_android.Networking.Services.ChannelServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 8/16/17.
 */

public class NewChannelJob extends Job {
    private ChannelRequest request;

    public NewChannelJob(ChannelRequest request) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.channel));

        this.request = request;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        ChannelServices services = NetContext.instance.create(ChannelServices.class);
        services.postNewChannel(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    EventBus.getDefault().post(new TaskCompleteRequest(AppConstant.CREATE_NEW_CHANNEL));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
